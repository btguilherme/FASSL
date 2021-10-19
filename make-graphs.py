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
ds_name = 'D6_BIC'
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
semi_classifiers = ['{}{}'.format(semi, sup) for sup in super_classifiers for semi in ['YATSI']]
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


metrics_title = {'test': 'Average and standard deviation for testing time per iteration',
				'train': 'Average and standard deviation for training time per iteration',
				'recall': 'Average and standard deviation for recall per iteration',
				'precision': 'Average and standard deviation for precision per iteration',
				'specificity': 'Average and standard deviation for specificity per iteration',
				'fmeasure': 'Average and standard deviation for F-measure per iteration',
				'roc': 'Average and standard deviation for ROC per iteration',
				'sensibility': 'Average and standard deviation for sensitivity per iteration',
				'kappa': 'Average and standard deviation for Kappa per iteration',
				'acc': 'Average and standard deviation for accuracy per iteration'}

metrics_y_label = {'test': 'Time (ms)',
				'train': 'Time (ms)',
				'recall': 'Rate',
				'precision': 'Rate',
				'specificity': 'Rate',
				'fmeasure': 'Rate',
				'roc': 'Rate',
				'sensibility': 'Rate',
				'kappa': 'Rate',
				'acc': 'Rate'}

metrics_x_label = {'test': 'Iteration (#)',
				'train': 'Iteration (#)',
				'recall': 'Iteration (#)',
				'precision': 'Iteration (#)',
				'specificity': 'Iteration (#)',
				'fmeasure': 'Iteration (#)',
				'roc': 'Iteration (#)',
				'sensibility': 'Iteration (#)',
				'kappa': 'Iteration (#)',
				'acc': 'Iteration (#)'}

colors = {'RDBS': 'dimgray',
			'RDS': 'indianred',
			'AFC': 'gold',
			'Clu': 'lightgreen',
			'MSTBE': 'lightskyblue',
			'Rand': 'lightpink'}

line_color = 'black'
std_color = 'dimgrey'
markers = {'RDBS': 'o',
			'RDS': '1',
			'AFC': 's',
			'Clu': 'P',
			'MSTBE': 'x',
			'Rand': 'd'}

zorders = {sorts[0]: 60, sorts[1]: 50, sorts[2]: 40, sorts[3]: 30, sorts[4]: 20, sorts[5]: 10}
# zorders = {sorts[0]: 60}

max_x = []
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
			max_x.append(max(indexes))
			label = '{}{}_{}'.format('k-means_' if actual_sort != 'Rand' else '', actual_sort if actual_sort != 'AFC' else 'IBE', actual_classifier)
			p, = plt.plot(indexes, avg, label=label, zorder=zorders[actual_sort], color=colors[actual_sort])
			plt.fill_between(indexes, avg-std_dev, avg+std_dev, alpha=0.35, color=colors[actual_sort])
			plots.update({actual_sort: p})
			title = metrics_title[actual_metric]
	temp = []
	for sort in sorts:
		temp.append(plots[sort])

	plt.xlim(0, min(max_x))
	# plt.ylim(0.4418417366946778-.1, 0.9392857142857143+.1)
	if actual_metric == 'test' or actual_metric == 'train':
		pass
	else:
		plt.ylim(0, 1)
	plt.title(title)
	plt.xlabel(metrics_x_label[metric])
	plt.ylabel(metrics_y_label[metric])
	plt.legend(frameon=False, handles=temp)
	plt.savefig('graphs/{}_{}.png'.format(actual_metric, actual_classifier), dpi=400)
	plt.clf()


exit()
for classifier, beans in files_by_classifier.items():
	for metric in metrics:
		max_x = []
		for bean in beans:
			if bean.metric_type == metric:
				with open('{}/{}'.format(folder, bean.path), 'r') as b:
					lines = b.readlines()
				lines = [line.strip() for line in lines]
				lines = np.array([line.split(' ') for line in lines])
				indexes = lines[:, 0].astype(int)
				avg = np.array(lines[:, 1].astype(float))
				std_dev = lines[:, 2].astype(float)
				if metric == 'acc' or metric == 'sensibility':
					avg /= 100
					std_dev /= 100
				max_x.append(max(indexes))
				label = '{}_{}'.format(bean.sort_type, bean.classifier_type)
				plt.plot(indexes, avg, label=label)
				plt.fill_between(indexes, avg-std_dev, avg+std_dev, alpha=0.5)
				title = metrics_title[bean.metric_type]

		plt.xlim(0, min(max_x))
		# plt.ylim(0.4418417366946778-.1, 0.9392857142857143+.1)
		plt.ylim(0, 1)
		plt.title(title)
		plt.xlabel(metrics_x_label[metric])
		plt.ylabel(metrics_y_label[metric])
		plt.legend(frameon=False)
		plt.savefig('graphs/{}_{}.png'.format(metric, classifier))
		plt.clf()
