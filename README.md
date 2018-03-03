# Framework of Active Semi-Supervised Learning
<<<<<<< HEAD
##Motivation

Due to databases have continuously grown, efforts have been performed in the attempt to solve the problem related to the large amount of unlabeled data in disproportion to the scarcity of labeled data. Another important issue is related to the tradeoff between the difficulty in obtaining annotations provided by a specialist and the need for a significant amount of annotated data in order to train and obtain a robust classifier. Active learning techniques jointly with semi-supervised learning are interesting in this context, since a smaller number of more informative samples previously selected (by the active learning strategy) and labeled by a specialist can propagate the labels to a set of unlabeled data (through the semi-supervised learning strategy). However, most of the literature works neglects the need for interactive response times that can be required by certain real applications. 

##Results

We propose a more effective and efficient active semi-supervised learning framework, including a new active learning strategy for biological context. An extensive experimental evaluation was performed, comparing our proposals with state-of-the-art works and different supervised and semi-supervised classifiers. From the obtained results we can observe the benefits of our proposal, which allows the classifier achieve higher acuracies more quickly with a reduced number of annotated samples. The selection criterion adopted by our learning strategy, based on diversity and uncertainty, enables the prioritization of the most informative boundary samples, minimizing the specialist annotation effort and the learning cycle.
=======

The Framework of Active Semi-Supervised Learning (FASSL) includes a new active learning strategy for biological context. An extensive experimental evaluation was performed, comparing our proposals with state-of-the-art works and different supervised and semi-supervised classifiers. From the obtained results we can observe the benefits of our proposal, which allows the classifier achieve higher acuracies more quickly with a reduced number of annotated samples. The selection criterion adopted by our learning strategy, based on diversity and uncertainty, enables the prioritization of the most informative boundary samples, minimizing the specialist annotation effort and the learning cycle.
>>>>>>> 66e5a46b534b957ea27f56cde410ad02079c28b4

##How to use it

###Cloning the project

First of all, you will need a platform to run the code. We recommend the **Netbeans IDE** (*https://netbeans.org/downloads/*) for open the project e run the tasks (the Java SE version is enough to run the project).

Then, you have to clone the source code to your personal computer. It can be done directly in Netbeans. To do so, click in the "Team" menu, choose `Git` then `Clone...`.

In the "Repository URL" blank space, put the URL: https://github.com/btguilherme/FASSL.git. `User` and `Password` can be leaved blank.

In the `Clone into` space, you should specify a destination folder (where the project will be cloned). Click `Finish`.

###Configuring the code execution

All the configuration is made in the `settings.properties` file. Next is described what each keyword means:

**`x-num-classes`**: In the selection of the most informative samples stage, it is selected from the learning set the same quantity of classes of the dataset have multiplied by `x-num-classes`. So, if a dataset presents 4 classes and `x-num-classes` is equals 2, then 8 samples will be selected in each iteration learning.

**`bases-save-path`**: The location where the splited files are in.

**`splits-sorted`**: The location where the splited and sorted files are in.

**`arff-path`**: The location where the original dataset file are in. The file has to be in ARFF format.

**`num-splits`**: The number of random splits will be made in the original dataset.

**`pct`**: Percentage of the original dataset that will be used as learning set. (values between `0` and `1`). `1 - pct` equals to the test set.

**`sort`**: The active learning method to be apply (case sensitive; for multiple methods, *blank space* between the techniques is required).

**`classifier`**: The classifier to be used (case sensitive; for multiple classifiers, *blank space* between them is required).


###Basic usage

1. Run `FASSL-Splitter` to split the dataset into Z2', Z2'' and Z3. Just run the file `FASSLSplitter.java` in `fassl.splitter` package. The splited files will be stored at *../FASSL/FASSL-Splitter/arff-files*. Each split will be identify by a hash code, concatenated in the beginning of the file name.

2. Since the split step is done, you are good to run `FASSL-Sort` project. Just run the file `FASSLSort.java` in `fassl.sort` package. The sorted files will be stored at *../FASSL/FASSL-Sort/arff-files-sorted*, and the sorting time for each *.arff* file will be stored at *../FASSL/txt-files*.

3. After Step 2, you have to open the project named `FASSL-SelectionClassification`. Just run the file `FASSLSelectionClassification.java` in `fassl.main package`. The samples used by each iteration of learning will be stored at *../FASSL/FASSL-SelectionClassification/arff-files-iterations*, and the measurements (i.e. accuracy, f-measure, precision, recall and ROC) will be stored at *../FASSL/txt-files*.

4. Then, to get the average and standard deviation of the measures, open the `FASSL-Information` project. Just run the file `FASSLInformation.java` in `fassl.information` package. The *.dat* files will be generated with iteration number, accuracy and standard deviation, and stored at *../FASSL/dat-files*.

If you already have splited files or even organized files by others active learning techniques, you can jump steps. Just put your files at the corresponding folder (i.e. sorted files at *../FASSL/FASSL-Sort/arff-files-sorted*).


##Contact

E-mail to [Guilherme Camargo](mailto:gcamargo@alunos.utfpr.edu.br).
