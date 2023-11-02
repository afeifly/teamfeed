import {Component, OnInit} from '@angular/core';
import * as d3 from 'd3';
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";

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
  private svg: any;
  private lineSvg: any;
  private margin = 20;
  private width = 550;
  private height = 230;
  // The radius of the pie chart is half the smallest side
  private radius = Math.min(this.width, this.height) / 2;
  private colors: any;
  private lineData = [];
  updateDate: any;

  private sub: any;
  bid: string;
  title: string;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
  ) {
  }


  ngOnInit(): void {
    this.updateDate = "";
    this.createSvg();

    this.sub = this.activatedRoute.params.subscribe(params => {
      this.bid = params['bid']; // (+) converts string 'id' to a number
      this.title = params['title'];
      console.log("::: bid="+this.bid + " t:"+this.title);
      const headers= new HttpHeaders({'Content-Type': 'application/json'});
      this.http.get<any>('/api/buildingsales/'+this.bid,
        { headers }
      ).subscribe(
        (response) => {
          // this.dataSource.data = response;
          console.log(response.buildingSales);
          this.lineData = response.buildingSales;
          this.drawLineChart();

          response.buildingSales.forEach((x,i)=>{

            if(i==(response.buildingSales.length-1)){
               this.pieData[0].Stars = x.sold;
              this.pieData[1].Stars = x.unsold;
              this.drawPieChart();
              //TODO update time
              this.updateDate = x.ts;
            }
            console.log(i);
            console.log(x.ts);
            console.log(x.percent);

          });
          // if(response.)
        },
        (error) => {
          console.log(error);
        }
      );
    });
  }
  onClickBC() {

    this.router.navigate(['/buildings']);
  }




  private createSvg(): void {
    this.svg = d3.select("figure#pie")
      .append("svg")
      .attr("width", this.width + 2 * this.margin)
      .attr("height", this.height + 2 * this.margin)
      .append("g")
      .attr(
        "transform",
        // "translate(" + (this.width + this.margin) / 2 + "," + (this.height + this.margin) / 2 + ")"
    "translate(" + (this.width/2 + this.margin) / 2 + "," + (this.height + this.margin) / 2 + ")"
      );
    this.lineSvg = d3.select("figure#lineC")
      .append("svg")
      .attr("width", this.width + 2 * this.margin)
      .attr("height", this.height + 2 * this.margin)
      .attr('transform', 'translate(' + this.margin + ',' + this.margin + ')');

  }

  private drawPieChart(): void {

    this.colors = d3.scaleOrdinal()
      .domain(this.pieData.map(d => d.Stars.toString()))
      .range(['#4daf4a', '#377eb8', '#ff7f00', "#c7d3ec", "#a5b8db", "#879cc4", "#677795", "#5a6782"]);
    const pie = d3.pie<any>().value((d: any) => Number(d.Stars));
    pie.padAngle(0.02);

    // Build the pie chart
    this.svg
      .selectAll('pieces')
      .data(pie(this.pieData))
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
      .data(pie(this.pieData))
      .enter()
      .append('text')
      .text((d: any) => d.data.Framework)
      .attr("transform", (d: any) => "translate(" + labelLocation.centroid(d) + ")")
      .style("text-anchor", "middle")
      .style("font-size", 12);
  }

  private drawLineChart(): void {

    const xScale = d3.scaleTime()
      .domain(d3.extent(this.lineData, d => {
        console.log("hahahahahahah");
        console.log(d.ts.substring(0,10));
        // return new Date(d.ts.substring(0,10));
        return new Date(d.ts);
      }))
      .range([0, this.width]);
    // xScale.invert(4);
    // xScale.nice(1);


    const yScale = d3.scaleLinear()
      // .domain([0, d3.max(this.data, d => d['percent'])])
      .domain([0, d3.max(this.lineData, d => d.percent)])
      .domain([0, 105])
      .range([this.height, 0]);

    // Create the line generator
    const line = d3.line()
    .x(d => {
      console.log("llllll");
      console.log(d);
      // console.log(d.ts);
      console.log(new Date(d["ts"]));
      console.log(new Date('2023-04-01'));
      return xScale(new Date(d["ts"]));})
    .y(d => yScale(d["percent"]));
      // .x(d => xScale(d['ts']))
      // .y(d => yScale(d['percent']));

    this.lineSvg.append("g")
      .attr("transform", "translate(" + this.margin + ",0)")
      .call(d3.axisLeft(yScale));

    this.lineSvg.append("g")
      .attr("transform", `translate(${this.margin}, ${this.height})`)
      .call(d3.axisBottom(xScale));

    this.lineSvg.append("path")
      .datum(this.lineData)
      .attr("fill", "none")
      .attr("stroke", "steelblue")
      .attr("transform", "translate(" + this.margin + ",0)")
      .attr("stroke-width", 2)
      .attr("d", line);


  }
}
