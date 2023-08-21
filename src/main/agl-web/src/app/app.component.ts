import {Component, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from "./user.service";
import {interval} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatSidenav} from "@angular/material/sidenav";
import {BreakpointObserver} from "@angular/cdk/layout";
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'angular-app';
   // user: string = sessionStorage.getItem("user");
  nickname: string = sessionStorage.getItem("user");
  uid: string = sessionStorage.getItem("user-id");
  icon_index: string = sessionStorage.getItem("user-icon");
  attack = 0;
  welcome: string = '';
  private sub: any;

  @ViewChild(MatSidenav)
  sidenav!: MatSidenav;
  constructor(
              private activeRouter: ActivatedRoute,
              private router: Router,
              private http: HttpClient,
              private snackBar: MatSnackBar,
              private observer: BreakpointObserver,
              private userService: UserService) {
    let currentTime = new Date();
    let hour = currentTime.getHours();
    if(hour>=0&&hour<9) {
      this.welcome = "🐦起来尿尿?"
    }else if(hour>=9&&hour<11){
      this.welcome = "摸鱼的一天"
    }else if(hour>=11&&hour<13){
      this.welcome = '🌞日当午'
    }else if(hour>=13&&hour<18){
      this.welcome = '☕️继续摸鱼 '
    }else if(hour>=18){
      this.welcome = '💪 今天你摸了吗? '
    }
    this.welcome += '   ';

    this.observer.observe(["(max-width: 800px)"]).subscribe((res) => {
      if (res.matches) {
        this.sidenav.mode = "over";
        this.sidenav.close();
      } else {
        this.sidenav.mode = "side";
        this.sidenav.open();
      }
    });
  }

  ngOnInit() {

    if(sessionStorage.getItem("user") != null ){
      this.userService.userLoggedIn.next(true);
    }
    this.userService.userLoggedIn.subscribe(loggedIn => {
      if (loggedIn) {
        // Show welcome message
        this.nickname = sessionStorage.getItem("user");
        this.uid = sessionStorage.getItem("user-id");
        this.icon_index = sessionStorage.getItem("user-icon");
        this.attack = parseInt(sessionStorage.getItem("attack"));
        console.log("ic == "+ this.icon_index);
      }else{
        this.nickname = "";
        this.icon_index = "";
      }
    });
  }
  onClickHome(){
    console.log('home') ;
    this.sidenav.close();
    if(sessionStorage.getItem("user") != null ) {
      this.userService.userLoggedIn.next(true);
      this.router.navigate(['/home']);
    }else{
      this.userService.userLoggedIn.next(false);
      this.router.navigate(['/login']);
    }
  }
  onClickLogOut() {
    sessionStorage.removeItem('user');
    sessionStorage.removeItem('user-icon');
    sessionStorage.removeItem('attack');
    this.userService.userLoggedIn.next(false);
    this.router.navigate(['/login']);
  }

  onClickName() {
    this.sub = this.activeRouter.params.subscribe(params => {
      const headers= new HttpHeaders({'Content-Type': 'application/json'});
      this.http.post<any>('/api/check_attack',
        {u_id: this.uid},
        { headers }
      ).subscribe(
        (response) => {
          if(response>2){
            //Can open and read.
            sessionStorage.setItem('attack',response);

            this.snackBar.open('当前体力: '+response, 'OK', {duration: 2000});
          }else{
            this.snackBar.open('体力耗尽, 明天再来吧. 多赞多留言,强身健体.', 'OK', {duration: 2000});
          }
        },
        (error) => {
          console.log(error);
          //TODO error and return;
        }
      );
    });

  }

  onClickHistory() {

    this.router.navigate(['/articles']);
    this.sidenav.close();
  }
}
