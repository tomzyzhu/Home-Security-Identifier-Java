# Home-Security-Identifier-Java (WATSecure)
A project constructed from APIs at Hack the North 2018, a hackathon at Waterloo, Ontario. Uses OpenCV to project and save images, which sent to an image recognition AI in the IBM Watson Cloud. The intent was to implement this software to provide a good way to send a text to one's phone when someone uses one's doorbell or when something triggers a motion sensor, as well as providing an image and an AI produced suggestion of who it is at the door. No actual product exists, only the working project files.

Demo from the hackathon: https://devpost.com/software/watsecure-io

## Executing the projects with NetBeans
Note: the project can be run on other IDEs, but I simply just used NetBeans

1. Download the files and also download + install NetBeans

2. Open the project with File-> Open Project (or Ctrl+Shift+O)
![alt text](https://github.com/tomzyzhu/Home-Security-Identifier-Java/blob/master/How%20to%20Images/1.png "Image 1")

3.Select the project and open it
![alt text](https://github.com/tomzyzhu/Home-Security-Identifier-Java/blob/master/How%20to%20Images/2.png "Image 2")

4.Right click the project and select "Clean and Build"
![alt text](https://github.com/tomzyzhu/Home-Security-Identifier-Java/blob/master/How%20to%20Images/3.png "Image 3")

5.Run Test.java 
![alt text](https://github.com/tomzyzhu/Home-Security-Identifier-Java/blob/master/How%20to%20Images/4.png "Image 4")

6.The program should be running if 2 windows pop up; 1 should be empty with the Title "Button Simulator" and 1 should take in live image feedback from the default webcam on your device. To run a query, simply click within the blank frame named "Button Simulator", and the output should show up in the console window.
![alt text](https://github.com/tomzyzhu/Home-Security-Identifier-Java/blob/master/How%20to%20Images/5.png "Image 5")

The more notable output comes from the JSON blurb, which is the query from the trained AI on IBM Watson.
### Example JSON Queries:
Below is a query with no classes (Note: This only happens if the image recognition software has a confidence below a certain threshold for all classes. The default value I've left the threshold at is 0.60)
```
{
  "custom_classes": 4,
  "images_processed": 1,
  "images": [
    {
      "image": "-temp-image.jpg",
      "classifiers": [
        {
          "name": "Default Custom Model",
          "classifier_id": "DefaultCustomModel_391163032",
          "classes": []              
        }
      ]
    }
  ]
}
```

Below is a query that has "Tom" as the most confident class with 0.776 confidence (the program only returns the class with the highest confidence value)

```
{
  "custom_classes": 4,
  "images_processed": 1,
  "images": [
    {
      "image": "-temp-image.jpg",
      "classifiers": [
        {
          "name": "Default Custom Model",
          "classifier_id": "DefaultCustomModel_391163032",
          "classes": [
            {
              "class": "Tom",    
              "score": 0.776     
            }
          ]
        }
      ]
    }
  ]
}
```
