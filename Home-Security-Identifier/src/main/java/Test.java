
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.bytedeco.javacv.*;

import static org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.cvFlip;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;

/**
 * Created by gtiwari on 1/3/2017.
 */
public class Test extends JPanel implements Runnable, ActionListener {

    private final int INTERVAL = 3000;///you may use interval
    private CanvasFrame canvas = new CanvasFrame("Web Cam");
    private FrameGrabber grabber = new VideoInputFrameGrabber(0); // 1 for next camera
    private OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
    private IplImage img;
    private MyPojo pojo;
    private JButton button = new JButton("Click here for a webcam image query");
    private static final double thresholdVal = 0.6;

    public static final String ACCOUNT_SID = "AC76568a4d43d590ecef217f22ab299aeb";
    public static final String AUTH_TOKEN = "530479b901c025b39f9eb7213320188d";

    public JButton getButton() {
        return button;
    }

    public void sendText(String name) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(new PhoneNumber("+16474094989"),
                new PhoneNumber("+12268871140"),
                "Hey! " + name + " is trying to get in.").create();
    }

    public void getImage() throws FileNotFoundException, IOException {
        IamOptions options = new IamOptions.Builder().apiKey("F4TaaiWSOyiHTRZ_cYo6kSE7e4WdTyJxEGEUg3xu7Lcg").build();

        VisualRecognition service = new VisualRecognition("2018-03-19", options);

        InputStream imagesStream = new FileInputStream("-temp-image.jpg");
        ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                .imagesFile(imagesStream)
                .imagesFilename("-temp-image.jpg")
                .threshold((float) thresholdVal)
                .owners(Arrays.asList("me"))
                .build();
        ClassifiedImages result = service.classify(classifyOptions).execute();

        ObjectMapper mapper = new ObjectMapper();
        pojo = mapper.readValue(result.toString(), MyPojo.class);

        System.out.println(result);
    }

    public Test() {
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    }

    //Interface function from Runnable 
    public void run() {

        int i = 0;
        try {

            //infinite loop takes input from the camera continuously till program stops
            grabber.start();
            while (true) {
                Frame frame = grabber.grab();

                img = converter.convert(frame);

                //the grabbed frame will be flipped, re-flip to make it right
                cvFlip(img, img, 1);// l-r = 90_degrees_steps_anti_clockwise

                //saves image to canvas
                canvas.showImage(converter.convert(img));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //main function
        JFrame window = new JFrame("Button Frame");
        Test gs = new Test();
        window.setSize(500, 300);
        window.setLocation(750, 250);
        window.setVisible(true);

        //button intialization
        gs.getButton().setLocation(0, 0);
        gs.getButton().setSize(750, 250);
        gs.getButton().addActionListener(gs);
        window.add(gs.getButton());

        window.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

        //start continuous thread
        Thread th = new Thread(gs);
        th.start();
    }
    public static void fakeText(String person){
        
        JOptionPane.showMessageDialog(null, "Hey! " + person + " is trying to get in.", "Fake Text", JOptionPane.INFORMATION_MESSAGE);
    }
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == button) {
            cvSaveImage("-temp-image.jpg", img);
            try {
                getImage();
                String tempName;
                try {
                    //Get name of whoever is at the door
                    tempName = pojo.getImages()[0].getClassifiers()[0].getClasses()[0].getClassif();

                } catch (Exception e) {
                    //If tere is no name, there will be a thrown error, so send message that it's a stranger
                    tempName ="A stranger";
                }
                //code below is for actually sending a text message, if given a working Twilio account and API key (see above for API keys)
                //sendText(tempName);
                
                //replace text code with instead an info box that pops up if the right message
                fakeText(tempName);
            } catch (Exception e) {
                //In the case that getImage() runs into an error processing the image
                e.printStackTrace();
            }
        }

    }

}
