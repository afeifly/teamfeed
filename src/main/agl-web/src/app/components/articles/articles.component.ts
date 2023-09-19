import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {MatTableDataSource} from "@angular/material/table";
import {MatDialog, MAT_DIALOG_DATA, MatDialogModule} from '@angular/material/dialog';
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from "@angular/material/button";
import {MatInputModule} from "@angular/material/input";
import {FormsModule} from "@angular/forms";
import {Component, Inject} from "@angular/core";
import {MatListModule} from "@angular/material/list";
import {MatBadgeModule} from "@angular/material/badge";
import {MatSnackBar} from "@angular/material/snack-bar";
import {DomSanitizer} from "@angular/platform-browser";
import {take} from "rxjs";

export interface DialogData {
  a_id: 0;
  comments: any;
  commentSize: 0;
}
@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.css']
})



export class ArticlesComponent {

  displayedColumns: string[] = ['position', 'title', 'likes', "date"];
  displayedColumns_mini: string[] = [ 'title', 'likes', "date"];
  dataSource = new MatTableDataSource();

  fid: string;
  title: string;
  uid: string;
  private sub: any;
  innerWidth: number;
  constructor(private activatedRoute: ActivatedRoute,
              private router: Router,
              private http: HttpClient,
              private snackBar: MatSnackBar,
              // public translate: DomSanitizer,
              public dialog: MatDialog) {
  }
  ngOnInit() {

    //TODO
    this.innerWidth = window.innerWidth;
    console.log(this.innerWidth);
    if(this.innerWidth<700){
      this.displayedColumns.splice(0,1);
    }else{
    }

    // this.snackBar.open('width = '+innerWidth,'OK');
    this.sub = this.activatedRoute.params.subscribe(params => {
      this.fid = params['fid']; // (+) converts string 'id' to a number
      this.title = params['title'];
      this.uid = sessionStorage.getItem('user-id');
      if(this.fid == null){

        //TODO goto history
        this.onClickGetBack();
        return ;
      }

      console.log("::: fid="+this.fid + " uid="+this.uid +" title="+this.title);
      const headers= new HttpHeaders({'Content-Type': 'application/json'});
      this.http.post<any>('/api/fetch_articles',
        {u_id: this.uid,f_id: this.fid},
        { headers }
      ).subscribe(
        (response) => {
          this.dataSource.data = response;
          console.log(response);
        },
        (error) => {
          console.log(error);
        }
      );
    });
  }

  commentResult : any[];

  onClickDialog(id,title,content){

    //TODO  not content, need detail
    console.log(":"+content);
    if(this.uid==null){
      // console.log("to login")
      // this.userService.userLoggedIn.next(false);
      this.router.navigate(['/login']);
    }
    // let tmp = content.replaceAll('<img ', '<img style="width: ' +
    //   (this.innerWidth-55)+'px; height: auto !important;" ');



    // content = this.translate.bypassSecurityTrustHtml(tmp);
    // title = title +"zzzx xd";

    this.sub = this.activatedRoute.params.subscribe(params => {
      const headers= new HttpHeaders({'Content-Type': 'application/json'});
      this.http.post<any>('/api/check_attack',
        {u_id: this.uid},
        { headers }
      ).subscribe(
        (response) => {
          if(response>2){
            //TODO send quest to content
            this.http.get<any>('/api/article/'+id,
            ).subscribe(
              (response) => {
                let tmp = response.content.replaceAll('<img ', '<img style="width: ' +
                  (this.innerWidth-55)+'px; height: auto !important;" ');
                //Can open and read.
                sessionStorage.setItem('attack',response);
                this.router.navigate(['/detail',
                  {a_id: id,
                    title: title,
                    content: tmp
                  }]);

              },
              (error) => {
                console.log(error);
              }
            );

          }else{
            //TODO not only strength less maybe not login
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

  onClickGetBack() {
    //TODO get history
    this.sub = this.activatedRoute.params.subscribe(params => {
      this.uid = sessionStorage.getItem('user-id');
      const headers= new HttpHeaders({'Content-Type': 'application/json'});
      this.http.post<any>('/api/fetch_history_articles',
        {u_id: this.uid},
        { headers }
      ).subscribe(
        (response) => {
          this.dataSource.data = response;
          console.log(response);
        },
        (error) => {
          console.log(error);
        }
      );
    });
  }
}

