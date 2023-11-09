import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import {Router} from "@angular/router";
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {UserService} from "../../user.service";
@Component({
  selector: 'app-log-in',
  templateUrl: './log-in.component.html',
  styleUrls: ['./log-in.component.css']
})
export class LogInComponent {

  username = '';
  password = '';
  nickname = '';
  constructor(private snackBar: MatSnackBar,
              private router: Router,
              private userService: UserService,
              private http: HttpClient) {}

  ngOnInit() {
    this.username = localStorage.getItem('user');
    this.password = localStorage.getItem('psw');
  }
  onClickLogin() {
    if(!this.username || this.username.length<5) {
      this.snackBar.open('Username is required, and should be more than 5 chars', 'OK', {duration: 2000});
      return;
    }
    if(!this.password || this.password.length<5) {
      this.snackBar.open('Password is required, and should be more than 5 chars', 'OK', {duration: 2000});
      return;
    }
    const headers= new HttpHeaders({'Content-Type': 'application/json'});
    this.http.post<any>('/api/login',
      {username: this.username,psw: this.password},
      { headers }
      ).subscribe(
      (response) => {
        if(response==null){
          this.snackBar.open('密码错误?', 'OK', {duration: 2000});
          return;
        }
        sessionStorage.setItem('user', response.nickname);
        sessionStorage.setItem('user-icon', response.iconIndex);
        sessionStorage.setItem('user-id', response.u_id);
        sessionStorage.setItem('user-attack', response.attack);
        localStorage.setItem('user', this.username);
        localStorage.setItem('psw', this.password);
        this.userService.userLoggedIn.next(true);
        this.router.navigate(['/home']);
      },
      (error) => {

        console.log(error);
      }
    );
  }
}
