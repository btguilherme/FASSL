# Framework of Active Semi-Supervised Learning
**Motivation**

Due to databases have continuously grown, efforts have been performed in the attempt to solve the problem related to the large amount of unlabeled data in disproportion to the scarcity of labeled data. Another important issue is related to the tradeoff between the difficulty in obtaining annotations provided by a specialist and the need for a significant amount of annotated data in order to train and obtain a robust classifier. Active learning techniques jointly with semi-supervised learning are interesting in this context, since a smaller number of more informative samples previously selected (by the active learning strategy) and labeled by a specialist can propagate the labels to a set of unlabeled data (through the semi-supervised learning strategy). However, most of the literature works neglects the need for interactive response times that can be required by certain real applications. 

**Results**

We propose a more effective and efficient active semi-supervised learning framework, including a new active learning strategy for biological context. An extensive experimental evaluation was performed, comparing our proposals with state-of-the-art works and different supervised and semi-supervised classifiers. From the obtained results we can observe the benefits of our proposal, which allows the classifier achieve higher acuracies more quickly with a reduced number of annotated samples. The selection criterion adopted by our learning strategy, based on diversity and uncertainty, enables the prioritization of the most informative boundary samples, minimizing the specialist annotation effort and the learning cycle.

**Contact**

:email:: gcamargo[at]alunos.utfpr.edu.br
