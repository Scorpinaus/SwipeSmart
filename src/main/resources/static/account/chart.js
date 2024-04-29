const chartData = {
	labels: ["Python", "Java", "javascript", "C#", "Others"],
	data: [30, 17, 10, 7, 36],
};


const categoryChart = document.querySelector(".category-chart");


new Chart(categoryChart, {
	type: "doughnut",
	data: {
		labels: chartData.labels,
		datasets: [
			{
				label: "Language",
				data: chartData.data,
			},
		],
	},
});