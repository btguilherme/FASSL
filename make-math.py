import matplotlib.pyplot as plt
import numpy as np
import os
import seaborn as sns
from matplotlib import colors as mcolors

sns.set(font='Franklin Gothic Book',
		rc={
 'axes.axisbelow': False,
 'axes.edgecolor': 'lightgrey',
 'axes.facecolor': 'None',
 'axes.grid': False,
 'axes.labelcolor': 'dimgrey',
 'axes.spines.right': False,
 'axes.spines.top': False,
 'figure.facecolor': 'white',
 'lines.solid_capstyle': 'round',
 'patch.edgecolor': 'w',
 'patch.force_edgecolor': True,
 'text.color': 'dimgrey',
 'xtick.bottom': False,
 'xtick.color': 'dimgrey',
 'xtick.direction': 'out',
 'xtick.top': False,
 'ytick.color': 'dimgrey',
 'ytick.direction': 'out',
 'ytick.left': False,
 'ytick.right': False})
sns.set_context("notebook", rc={"font.size":12,
								"axes.titlesize":14,
								"axes.labelsize":12})


class Bean:

	def __init__(self, path, model_uuid, dataset_name, sort_type,
		metric_type, classifier_type):

		self.path = path
		self.model_uuid = model_uuid
		self.dataset_name = dataset_name
		self.sort_type = sort_type
		self.metric_type = metric_type
		self.classifier_type = classifier_type

	def __str__(self):
		s = 'path: {}\nmodel_uuid: {}\ndataset_name: {}\nsort_type: {}\nmetric_type: {}\nclassifier_type: {}\n'.format(
			self.path, self.model_uuid, self.dataset_name, self.sort_type, self.metric_type, self.classifier_type)
		return s



folder = 'dat-files'
ds_name = 'inception_v3_header_'
files = os.listdir(folder)
# files = sorted([f.split(ds_name)[1] for f in files])

beans = []
for f in files:
	splitted = f.split('@')
	model_uuid = splitted[0]
	splitted = splitted[1].split('.')[0]
	splitted = splitted.split('_')
	classifier_type = splitted[-1]
	metric_type = splitted[-2]
	sort_type = splitted[-3]
	dataset_name = ''.join(splitted[:-3])
	bean = Bean(f, model_uuid, dataset_name, sort_type, metric_type, classifier_type)
	beans.append(bean)

super_classifiers = ['OPF', 'RF', 'SVM']
semi_classifiers = ['{}{}'.format(semi, sup) for sup in super_classifiers for semi in ['CollectiveWrapper', 'YATSI']]
metrics = ['acc','test','train','recall','precision','specificity','fmeasure','roc','sensibility','kappa']
sorts = ['RDBS','RDS','AFC','Clu','MSTBE','Rand']
classifiers = super_classifiers
classifiers.extend(semi_classifiers)

sort_metric_classifier_pair = ['{}_{}_{}'.format(
	sort, metric, classifier) for sort in sorts for metric in metrics for classifier in classifiers]

files_by_classifier = {classifier:[] for classifier in classifiers}
for bean in beans:
	for classifier in classifiers:
		if bean.classifier_type == classifier:
			files_by_classifier[classifier].append(bean)



zorders = {sorts[0]: 60, sorts[1]: 50, sorts[2]: 40, sorts[3]: 30, sorts[4]: 20, sorts[5]: 10}

to_save = {}
for pair in sort_metric_classifier_pair:
	s = pair.split('_')
	metric = s[-2]
	classifier = s[-1]
	plots = {}
	for f in files:
		if '{}_{}'.format(metric, classifier) in f:
			actual_sort = f.split('_')[-3]
			actual_metric = metric
			actual_classifier = classifier
			with open('{}/{}'.format(folder, f), 'r') as b:
				lines = b.readlines()
			lines = [line.strip() for line in lines]
			lines = np.array([line.split(' ') for line in lines])
			indexes = lines[:, 0].astype(int)
			avg = np.array(lines[:, 1].astype(float))
			std_dev = lines[:, 2].astype(float)
			if actual_metric == 'acc' or actual_metric == 'sensibility' or actual_metric == 'specificity':
				avg /= 100
				std_dev /= 100
			key = '{}_{}_{}'.format(actual_sort, actual_metric, actual_classifier)
			to_save.update({key: zip(avg, std_dev)})


for k,v in to_save.items():
	if 'Rand_acc' in k:
		print(k)
		for media, desvio in v:
			print(media, desvio)


	# if actual_metric == 'test' or actual_metric == 'train':
	# 	pass
	# else:
	# 	plt.ylim(0, 1)
	# plt.title(title)
	# plt.xlabel(metrics_x_label[metric])
	# plt.ylabel(metrics_y_label[metric])
	# plt.legend(frameon=False, handles=temp)
	# plt.savefig('graphs/{}_{}.png'.format(actual_metric, actual_classifier), dpi=400)
	# plt.clf()
