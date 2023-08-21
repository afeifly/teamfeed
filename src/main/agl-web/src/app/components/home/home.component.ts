import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import {MatTableDataSource} from "@angular/material/table";



@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})



export class HomeComponent {
  displayedColumns: string[] = ['position', 'title',  "date"];
  dataSource = new MatTableDataSource();
  constructor(private router: Router,
              private http: HttpClient) {}
  ngOnInit() {
    this.http.get<any>('/api/feeds').subscribe(
      (response) => {
        this.dataSource.data = response;
        console.log(response);
      },
      (error) => {
        console.log(error);
      }
    );
  }
  onClickFeed(fid,title){
    console.log("click "+fid+" "+title);
    this.router.navigate(['/articles', {fid: fid, title: title}]);
  }

}
