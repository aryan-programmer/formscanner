FormScanner
===

### Fork of https://sourceforge.net/p/formscanner/code/ci/1.1.4/tree/

FormScanner is an OMR (Optical Mark Recognition) software that automatically marks multiple-choice papers. 
FormScanner not bind you to use a default template of the form, but gives you the ability to use a custom template created from a simple scan of a blank form.
The modules can be scanned as images with a simple scanner and processed with FormScanner software.
All the collected information can be easily exported to a spreadsheet.

---

# Modifications

The "Start all images scan" button saves the images, with the response points  marked as red circles, in a folder relative to the images as "parsed_images_*timestamp*".

Build requirements
===

* Java 9
* Maven
* Git

---

Compiling the software
===

Clone the project

```
mvn clean install
```