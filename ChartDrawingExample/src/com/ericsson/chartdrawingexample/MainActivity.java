package com.ericsson.chartdrawingexample;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;

import com.ericsson.json.JSONParser;

public class MainActivity extends Activity {

	private String TAG_IZMIR = "http://api.openweathermap.org/data/2.5/weather?q=Izmir&mode=json&units=metric";
	private String TAG_CAPETOWN = "http://api.openweathermap.org/data/2.5/weather?q=Cape%20Town&mode=json&units=metric";
	private String TAG_LONDON = "http://api.openweathermap.org/data/2.5/weather?q=London&mode=json&units=metric";
	private String TAG_MELBOURNE = "http://api.openweathermap.org/data/2.5/weather?q=Melbourne&mode=json&units=metric";
	private String TAG_NEWYORK = "http://api.openweathermap.org/data/2.5/weather?q=New%20York&mode=json&units=metric";
	private String[] url_list = { TAG_IZMIR, TAG_CAPETOWN, TAG_LONDON, TAG_MELBOURNE, TAG_NEWYORK };
	private JSONParser jsonParser;
	ArrayList<String> cities;
	ArrayList<Double> izmirList;
	ArrayList<Double> capetownList;
	ArrayList<Double> londonList;
	ArrayList<Double> melbourneList;
	ArrayList<Double> newyorkList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainpage);

		cities = new ArrayList<String>();
		for (int i = 0; i < url_list.length; i++) {
			getCities(i);
		}

		XYMultipleSeriesRenderer renderer = getBarRenderer();
		myChartSettings(renderer);
		Intent intent = ChartFactory.getBarChartIntent(this, getBarDataset(), renderer, Type.DEFAULT);
		startActivity(intent);
	}

	private XYMultipleSeriesDataset getBarDataset() {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		ArrayList<String> legendTitles = new ArrayList<String>();
		legendTitles.add("Temp");
		legendTitles.add("Temp Max");
		legendTitles.add("Temp Min");

		for (int i = 0; i < 3; i++) {
			CategorySeries series = new CategorySeries(legendTitles.get(i));
			for (int k = 0; k < 5; k++) {
				switch (k) {
				case 0:
					series.add(izmirList.get(i));
					break;
				case 1:
					series.add(capetownList.get(i));
					break;
				case 2:
					series.add(londonList.get(i));
					break;
				case 3:
					series.add(melbourneList.get(i));
					break;
				case 4:
					series.add(newyorkList.get(i));
					break;
				default:
					break;
				}
			}

			dataset.addSeries(series.toXYSeries());
		}

		return dataset;
	}

	public XYMultipleSeriesRenderer getBarRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setMargins(new int[] { 40, 40, 35, 10 });
		SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		r.setColor(Color.BLUE);
		renderer.addSeriesRenderer(r);
		r = new SimpleSeriesRenderer();
		r.setColor(Color.RED);
		renderer.addSeriesRenderer(r);
		r = new SimpleSeriesRenderer();
		r.setColor(Color.GREEN);
		renderer.addSeriesRenderer(r);
		return renderer;
	}

	private void myChartSettings(XYMultipleSeriesRenderer renderer) {
		renderer.setChartTitle("Weather");
		renderer.setXAxisMin(0.5);
		renderer.setXAxisMax(10.5);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(50);

		for (int i = 0; i < cities.size(); i++) {
			renderer.addXTextLabel(i + 1, cities.get(i));
		}

		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setBarSpacing(0.5);
		renderer.setXTitle("Cities");
		renderer.setYTitle("Tempurateres");
		renderer.setShowGrid(true);
		renderer.setGridColor(Color.GRAY);
		renderer.setXLabels(0);
	}

	public void getCities(int i) {
		jsonParser = new JSONParser(url_list[i]);
		jsonParser.fetchJSON();

		while (jsonParser.parsingComplete) {
		}

		cities.add(jsonParser.weatherJSON.getName());

		switch (i) {
		case 0:
			izmirList = new ArrayList<Double>();
			izmirList.add(Double.valueOf(jsonParser.weatherJSON.getTemp()));
			izmirList.add(Double.valueOf(jsonParser.weatherJSON.getTemp_max()));
			izmirList.add(Double.valueOf(jsonParser.weatherJSON.getTemp_min()));
			break;
		case 1:
			capetownList = new ArrayList<Double>();
			capetownList.add(Double.valueOf(jsonParser.weatherJSON.getTemp()));
			capetownList.add(Double.valueOf(jsonParser.weatherJSON.getTemp_max()));
			capetownList.add(Double.valueOf(jsonParser.weatherJSON.getTemp_min()));
			break;
		case 2:
			londonList = new ArrayList<Double>();
			londonList.add(Double.valueOf(jsonParser.weatherJSON.getTemp()));
			londonList.add(Double.valueOf(jsonParser.weatherJSON.getTemp_max()));
			londonList.add(Double.valueOf(jsonParser.weatherJSON.getTemp_min()));
			break;
		case 3:
			melbourneList = new ArrayList<Double>();
			melbourneList.add(Double.valueOf(jsonParser.weatherJSON.getTemp()));
			melbourneList.add(Double.valueOf(jsonParser.weatherJSON.getTemp_max()));
			melbourneList.add(Double.valueOf(jsonParser.weatherJSON.getTemp_min()));
			break;
		case 4:
			newyorkList = new ArrayList<Double>();
			newyorkList.add(Double.valueOf(jsonParser.weatherJSON.getTemp()));
			newyorkList.add(Double.valueOf(jsonParser.weatherJSON.getTemp_max()));
			newyorkList.add(Double.valueOf(jsonParser.weatherJSON.getTemp_min()));
			break;
		default:
			break;
		}
	}
}
