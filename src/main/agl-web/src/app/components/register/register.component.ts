import { Component } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {UserService} from "../../user.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  username = '';
  password1 = '';
  password2 = '';
  constructor(private router: Router,
              private snackBar: MatSnackBar,
              private userService: UserService,
              private http: HttpClient) { }
  ngOnInit() {
  }
  onClickRegister() {

    if(!this.username || this.username.length<5) {
      this.snackBar.open('Username is required, and should be more than 5 chars', 'OK', {duration: 2000});
      return;
    }
    if(!this.password1 || this.password1.length<5) {
      this.snackBar.open('Password is required, and should be more than 5 chars', 'OK{\n' +
        '  duration: 3000\n' +
        '}');
      return;
    }
    if(this.password1 != this.password2){
      this.snackBar.open('Twice password input do not match.', 'OK', {duration: 2000});
      return;
    }

    const headers= new HttpHeaders({'Content-Type': 'application/json'});
    this.http.post<any>('/api/users',
      {username: this.username,psw: this.password1},
      { headers }
    ).subscribe(
      (response) => {
        this.snackBar.open('Register successful.', 'OK', {duration: 2000});
        this.userService.userLoggedIn.next(false);
        this.router.navigate(['/login']);
      },
      (error) => {
        console.log(error);
        this.snackBar.open(error, 'OK', {duration: 2000});
      }
    );
  }
}
