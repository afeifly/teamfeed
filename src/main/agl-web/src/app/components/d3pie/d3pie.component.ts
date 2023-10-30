import {Component, OnInit} from '@angular/core';
import * as d3 from 'd3';

@Component({
  selector: 'app-d3pie',
  templateUrl: './d3pie.component.html',
  styleUrls: ['./d3pie.component.css']
})
export class D3pieComponent {


  private svg: any;
  private lineSvg: any;
  private margin = 25;
  private width = 320;
  private height = 200;
  // The radius of the pie chart is half the smallest side
  private radius = Math.min(this.width, this.height) / 2;
  private colors: any;

  constructor() {
  }

  ngOnInit(): void {
    this.createSvg();
    this.drawPieChart();
    this.drawLineChart();
  }

  private createSvg(): void {
    this.svg = d3.select("figure#pie")
      .append("svg")
      .attr("width", this.width + 2 * this.margin)
      .attr("height", this.height + 2 * this.margin)
      .append("g")
      .attr(
        "transform",
        "translate(" + (this.width + this.margin) / 2 + "," + (this.height + this.margin) / 2 + ")"
      );
    this.lineSvg = d3.select("figure#lineC")
      .append("svg")
      .attr("width", this.width + 2 * this.margin)
      .attr("height", this.height + 2 * this.margin)
      .attr('transform', 'translate(' + this.margin + ',' + this.margin + ')');

  }

  private drawPieChart(): void {

    const pieData = [
      {"Framework": "已备案", "Stars": "65", "Released": "2014"},
      {"Framework": "期房待售", "Stars": "121", "Released": "2011"},
    ];

    this.colors = d3.scaleOrdinal()
      .domain(pieData.map(d => d.Stars.toString()))
      .range(['#4daf4a', '#377eb8', '#ff7f00', "#c7d3ec", "#a5b8db", "#879cc4", "#677795", "#5a6782"]);
    // Compute the position of each group on the pie:
    const pie = d3.pie<any>().value((d: any) => Number(d.Stars));
    pie.padAngle(0.02);

    // Build the pie chart
    this.svg
      .selectAll('pieces')
      .data(pie(pieData))
      .enter()
      .append('path')
      .attr('d', d3.arc()
        .innerRadius(80)
        .outerRadius(this.radius)
      )
      .attr('fill', (d: any, i: any) => (this.colors(i)))
      .attr("stroke", "#121926");
//       .style("stroke-width", "1px");

    // Add labels
    const labelLocation = d3.arc()
      .outerRadius(this.radius + 100)
      .innerRadius(50);

    this.svg
      .selectAll('pieces')
      .data(pie(pieData))
      .enter()
      .append('text')
      .text((d: any) => d.data.Framework)
      .attr("transform", (d: any) => "translate(" + labelLocation.centroid(d) + ")")
      .style("text-anchor", "middle")
      .style("font-size", 15);
  }

  private drawLineChart(): void {
    const data = [
      {date: new Date('2023-01-01'), value: 266},
      {date: new Date('2023-02-01'), value: 174},
      {date: new Date('2023-03-01'), value: 155},
      {date: new Date('2023-04-01'), value: 145},
    ];
    const xScale = d3.scaleTime()
      .domain(d3.extent(data, d => d['date']))
      .range([0, this.width]);
    xScale.nice(1);


    const yScale = d3.scaleLinear()
      .domain([0, d3.max(data, d => d['value'])])
      .range([this.height, 0]);

    // Create the line generator
    const line = d3.line()
      .x(d => xScale(d['date']))
      .y(d => yScale(d['value']));

    this.lineSvg.append("g")
      .attr("transform", "translate(" + this.margin + ",0)")
      .call(d3.axisLeft(yScale));

    this.lineSvg.append("g")
      .attr("transform", `translate(${this.margin}, ${this.height})`)
      .call(d3.axisBottom(xScale));

    this.lineSvg.append("path")
      .datum(data)
      .attr("fill", "none")
      .attr("stroke", "steelblue")
      .attr("transform", "translate(" + this.margin + ",0)")
      .attr("stroke-width", 2)
      .attr("d", line);


  }
}
