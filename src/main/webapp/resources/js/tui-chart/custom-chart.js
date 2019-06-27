/******************************************************************************************************************************************************************
 * 파일명 : custom-chart.js
 * 제작자 : kbr
 * 일  자 : 2019-05-10
 * 내  용 : chart script
 ******************************************************************************************************************************************************************/

/*======================================================================== Toast Chart UI ========================================================================*/ 

jQuery(document).ready(function(){


/*----------------------------------------------------------------------------------------------------------------------------------------------------------------
//등록자화면 > 앱 사용현황 목록 : registAppUseStatus-chart.html(사용앱)
- 사용앱_도넛차트: registApp_donut
------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
var container = document.getElementById('registApp_donut');
var data = {
	categories: ['사용 앱'],
	series: [
		{
			name: '마이크로그리드(MG) 플랫폼 서비스',
			data: 10
		},
		{
			name: '수요자원시장 컨설팅 시스템',
			data: 10
		},
		{
			name: '전기자동차 에너지 발전 시스템',
			data: 10
		},
		{
			name: 'test_data',
			data: 10
		},
		{
			name: 'test_data',
			data: 10
		},
		{
			name: 'test_data',
			data: 10
		},
		{
			name: 'test_data',
			data: 10
		},
		{
			name: 'test_data',
			data: 10
		},
		{
			name: 'test_data',
			data: 10
		},
		{
			name: 'test_data',
			data: 10
		},
		{
			name: 'test_data',
			data: 10
		},
		{
			name: 'test_data',
			data: 10
		},
		{
			name: 'test_data',
			data: 10
		},
		{
			name: 'test_data',
			data: 10
		},
		{
			name: 'test_data',
			data: 10
		},
		{
			name: 'test_data',
			data: 10
		},
		{
			name: 'test_data',
			data: 10
		},
		{
			name: 'test_data',
			data: 10
		},
		{
			name: 'test_data',
			data: 10
		},
		{
			name: 'test_data',
			data: 10
		}
	]
};
var options = {
	chart: {
		width: 280,
		height: 240,
		//title: '사용 앱',
		format: function(value, chartType, areaType, valuetype) {
			if (areaType === 'makingSeriesLabel') { // formatting at series area
				value = value + '%';
			}

			return value;
		}
	},
	series: {
		radiusRange: ['50%', '100%'],
		showLabel: false
	},
	tooltip: {
		suffix: '%'
	},
	legend: {
		visible: false
	},
	chartExportMenu : {
		visible: false
	}
};
var theme = {
	series: {
		/*colors: ['#0d87b8','#f3be14','#bbbcbc'],*/
		label: {
			color: '#fff',
			fontFamily: 'Montserrat',
			fontSize:14,
			fontWeight:'500'
		}
	
	}
};

// For apply theme

tui.chart.registerTheme('myTheme', theme);
options.theme = 'myTheme';

tui.chart.pieChart(container, data, options);



/*----------------------------------------------------------------------------------------------------------------------------------------------------------------
//등록자화면 > 앱 사용현황 목록 : registAppUseStatus-chart.html(사용추이)
- 사용앱_도넛차트: registApp_line
------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	var container = document.getElementById('registApp_line');
	var data = {
		categories: ['01/01/2016', '02/01/2016', '03/01/2016', '04/01/2016', '05/01/2016', '06/01/2016', '07/01/2016', '08/01/2016', '09/01/2016', '10/01/2016', '11/01/2016', '12/01/2016'],
		series: [
			{
				name: 'Seoul',
				data: [-3.5, -1.1, 4.0, 11.3, 17.5, 21.5, 24.9, 25.2, 20.4, 13.9, 6.6, -0.6]
			},
			{
				name: 'Seattle',
				data: [3.8, 5.6, 7.0, 9.1, 12.4, 15.3, 17.5, 17.8, 15.0, 10.6, 6.4, 3.7]
			},
			{
				name: 'Sydney',
				data: [22.1, 22.0, 20.9, 18.3, 15.2, 12.8, 11.8, 13.0, 15.2, 17.6, 19.4, 21.2]
			},
			{
				name: 'Moskva',
				data: [-10.3, -9.1, -4.1, 4.4, 12.2, 16.3, 18.5, 16.7, 10.9, 4.2, -2.0, -7.5]
			},
			{
				name: 'Jungfrau',
				data: [-13.2, -13.7, -13.1, -10.3, -6.1, -3.2, 0.0, -0.1, -1.8, -4.5, -9.0, -10.9]
			},
			{
				name: 'Jungfrau',
				data: [5.2, 1.3, 6.8, 10.5, 14.4, 19.8, 22.6, 16.3, 15.5, 10.2, 12.6, 17.0]
			}
		]
	};
	var options = {
		chart: {
			width: 700,
			height: 250,
			//title: '24-hr Average Temperature'
		},
		yAxis: {
			//title: 'Temperature (Celsius)',
		},
		xAxis: {
			title: 'Month',
			pointOnColumn: true,
			dateFormat: 'MMM',
			tickInterval: 'auto'
		},
		series: {
			showDot: false,
			zoomable: true
		},
		tooltip: {
			suffix: '°C'
		},
		legend: {
			visible:false
		},
		chartExportMenu : {
		visible: false
		},
		plot: {
			bands: [
				{
					range: ['03/01/2016', '05/01/2016'],
					color: 'gray',
					opacity: 0.2
				}
			],
			lines: [
				{
					value: '03/01/2016',
					color: '#fa2828'
				}
			]
		}
	};
	var theme = {
		series: {
			pointWidth: 1,
			colors: [
				'#83b14e', '#458a3f', '#295ba0', '#2a4175', '#289399',
				'#289399', '#617178', '#8a9a9a', '#516f7d', '#dddddd'
			]
		}
	};
	// For apply theme
	// tui.chart.registerTheme('myTheme', theme);
	// options.theme = 'myTheme';
	var chart = tui.chart.lineChart(container, data, options);

});

