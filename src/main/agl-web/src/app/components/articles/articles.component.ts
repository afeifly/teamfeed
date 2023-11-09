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

    this.innerWidth = window.innerWidth;
    if(this.innerWidth<700){
      this.displayedColumns.splice(0,1);
    }else{
    }

    this.sub = this.activatedRoute.params.subscribe(params => {
      this.fid = params['fid']; // (+) converts string 'id' to a number
      this.title = params['title'];
      this.uid = sessionStorage.getItem('user-id');
      if(this.fid == null){

        this.onClickGetBack();
        return ;
      }

      const headers= new HttpHeaders({'Content-Type': 'application/json'});
      this.http.post<any>('/api/fetch_articles',
        {u_id: this.uid,f_id: this.fid},
        { headers }
      ).subscribe(
        (response) => {
          this.dataSource.data = response;
        },
        (error) => {
          console.log(error);
        }
      );
    });
  }

  commentResult : any[];

  onClickDialog(id,title,content){

    if(this.uid==null){
      this.router.navigate(['/login']);
    }

    this.sub = this.activatedRoute.params.subscribe(params => {
      const headers= new HttpHeaders({'Content-Type': 'application/json'});
      this.http.post<any>('/api/check_attack',
        {u_id: this.uid},
        { headers }
      ).subscribe(
        (response) => {
          if(response>2){

            sessionStorage.setItem('attack',response);
            this.router.navigate(['/detail',
                  {a_id: id,
                    title: title,
                    // content: tmp
                  }]);

          }else{
            this.snackBar.open('体力耗尽, 明天再来吧. 多赞多留言,强身健体.', 'OK', {duration: 2000});
          }


        },
        (error) => {
          console.log(error);
        }
      );
    });



  }

  onClickGetBack() {
    this.sub = this.activatedRoute.params.subscribe(params => {
      this.uid = sessionStorage.getItem('user-id');
      const headers= new HttpHeaders({'Content-Type': 'application/json'});
      this.http.post<any>('/api/fetch_history_articles',
        {u_id: this.uid},
        { headers }
      ).subscribe(
        (response) => {
          this.dataSource.data = response;
        },
        (error) => {
          console.log(error);
        }
      );
    });
  }
}

