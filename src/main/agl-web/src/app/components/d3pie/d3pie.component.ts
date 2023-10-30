import { Component, OnInit } from '@angular/core';
import * as d3 from 'd3';

@Component({
  selector: 'app-d3pie',
  templateUrl: './d3pie.component.html',
  styleUrls: ['./d3pie.component.css']
})
export class D3pieComponent {

  private pieData = [
    {"Framework": "已备案", "Stars": "65", "Released": "2014"},
    {"Framework": "期房待售", "Stars": "121", "Released": "2011"},
  ];
  private lineData = [
    {ts: new Date('2023-01-01'), value: 10},
    {ts: new Date('2023-02-01'), value: 10},
    {ts: new Date('2023-03-01'), value: 15},
    {ts: new Date('2023-04-01'), value: 17},
  ]

  private svg: any;
  private lineSvg: any;
  private margin = 15;
  private width = 320;
  private height = 200;
  // The radius of the pie chart is half the smallest side
  private radius = Math.min(this.width, this.height) / 2 - this.margin;
  private colors: any;

  constructor() {
  }

  ngOnInit(): void {
    this.createSvg();
    this.createColors();
    this.drawPieChart();
    this.drawLineChart();
  }

  private createSvg(): void {
    this.svg = d3.select("figure#pie")
      .append("svg")
      .attr("width", this.width)
      .attr("height", this.height)
      .append("g")
      .attr(
        "transform",
        "translate(" + this.width / 2 + "," + this.height / 2 + ")"
      );
    this.lineSvg = d3.select("figure#lineC")
      .append("svg")
      .attr("width", this.width+50)
      .attr("height", this.height+30)
  }
  private createColors(): void {
    this.colors = d3.scaleOrdinal()
      .domain(this.pieData.map(d => d.Stars.toString()))
      .range(['#4daf4a','#377eb8','#ff7f00',"#c7d3ec", "#a5b8db", "#879cc4", "#677795", "#5a6782"]);
  }
  private drawPieChart(): void {
    // Compute the position of each group on the pie:
    const pie = d3.pie<any>().value((d: any) => Number(d.Stars));
    pie.padAngle(0.02);

    // Build the pie chart
    this.svg
      .selectAll('pieces')
      .data(pie(this.pieData))
      .enter()
      .append('path')
      .attr('d', d3.arc()
        .innerRadius(70)
        .outerRadius(this.radius)
      )
      .attr('fill', (d: any, i: any) => (this.colors(i)))
      .attr("stroke", "#121926");
//       .style("stroke-width", "1px");

    // Add labels
    const labelLocation = d3.arc()
      .outerRadius(this.radius+50)
      .innerRadius(50);

    this.svg
      .selectAll('pieces')
      .data(pie(this.pieData))
      .enter()
      .append('text')
      .text((d: any)=> d.data.Framework)
      .attr("transform", (d: any) => "translate(" + labelLocation.centroid(d) + ")")
      .style("text-anchor", "middle")
      .style("font-size", 15);
  }

  private drawLineChart(): void {
    const data = [
       { date: new Date('2023-01-01'), value: 14 },
       { date: new Date('2023-02-01'), value: 22 },
       { date: new Date('2023-03-01'), value: 32 },
    ];
    const xScale = d3.scaleTime()
          .domain(d3.extent(data, d => d['date']))
          .range([0, this.width]);

        const yScale = d3.scaleLinear()
//           .domain([0, d3.max(data, d => d['value'])])
          .domain([0, d3.max(data, d => d['value'])])
          .range([this.height, 0]);

        // Create the line generator
        const line = d3.line()
//           .x(d => xScale(d.date))
          .x(d => xScale(d['date']))
//           .y(d => yScale(d.value));
          .y(d => yScale(d['value']));

    this.lineSvg.append("g")
//       .attr("transform", "translate(" + margin.left + "0,)")
      .call(d3.axisLeft(yScale));

    this.lineSvg.append("path")
      .datum(data)
      .attr("fill", "none")
      .attr("stroke","steelblue")
      .attr("stroke-width", 2)
      .attr("d", line);

    this.lineSvg.append("g")
      .attr("transform", `translate(0, ${this.height})`)
      .call(d3.axisBottom(xScale));

  }
}
