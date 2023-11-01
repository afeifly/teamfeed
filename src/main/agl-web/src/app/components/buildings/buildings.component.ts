import { Component } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-buildings',
  templateUrl: './buildings.component.html',
  styleUrls: ['./buildings.component.css']
})
export class BuildingsComponent {

  array = [
    {title: "t1 "},
    {title: "t2 "},
    {title: "t3 "},
  ]
  constructor(private activatedRoute: ActivatedRoute,
              private router: Router,
              private http: HttpClient,
              private snackBar: MatSnackBar,
              public dialog: MatDialog) {
  }
  ngOnInit() {
    const headers= new HttpHeaders({'Content-Type': 'application/json'});
    this.http.get<any>('/api/buildings',
      { headers }
    ).subscribe(
      (response) => {
        console.log(response);
        this.array = response;
      },
      (error) => {
        console.log(error);
      }
    );
  }
}
