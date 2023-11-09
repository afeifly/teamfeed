import { Component, ElementRef, Inject, Pipe, PipeTransform, SecurityContext, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
import { DatePipe, Location, NgForOf, NgIf } from '@angular/common';
import { MAT_DIALOG_DATA, MatDialog, MatDialogModule, MatDialogRef } from "@angular/material/dialog";
import { MatIconModule } from "@angular/material/icon";
import { MatButtonModule } from "@angular/material/button";
import { MatInputModule } from "@angular/material/input";
import { FormsModule } from "@angular/forms";
import { MatListModule } from "@angular/material/list";
import { MatBadgeModule } from "@angular/material/badge";
import { MatSnackBar } from "@angular/material/snack-bar";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Title } from "@angular/platform-browser";

export interface DialogData {
  a_id: 0;
  comments: any;
  commentSize: 0;
}
@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})


export class DetailComponent {

  private sub: any;
  title: string;
  a_id: number;
  like: number;
  uid: string;
  contentStr: string;
  comments: any[];
  commentSize: number;
  innerWidth: number;

  @ViewChild('mass_element') mass_element: ElementRef;
  constructor(private activatedRoute: ActivatedRoute,
    private router: Router,
    public translate: DomSanitizer,
    private location: Location,
    private http: HttpClient,
    public dialog: MatDialog,
    private titleService: Title,
  ) {
    this.innerWidth = window.innerWidth;
    this.sub = this.activatedRoute.params.subscribe(params => {
      this.a_id = params['a_id'];
      this.title = params['title'];
      this.titleService.setTitle(this.title);
      this.contentStr = "" + params['content'];
      this.comments = params['comments']
    });

    this.uid = sessionStorage.getItem('user-id');
  }

  ngOnInit() {


    this.http.get<any>('/api/article/' + this.a_id,
    ).subscribe(
      (response) => {

        let tmp = response.content.replaceAll('<img ', '<img style="width: ' +
          (this.innerWidth - 55) + 'px; height: auto !important;" ');
        this.contentStr = tmp;
      });
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    this.like = 0;
    this.sub = this.activatedRoute.params.subscribe(params => {
      this.http.post<any>('/api/comment_articles',
        { fk_a_id: this.a_id, fk_u_id: this.uid },
        { headers }
      ).subscribe(
        (response) => {
          this.comments = response;
          this.commentSize = this.comments.length;
        },
        (error) => {
          console.log(error);
        }
      );
    });

  }
  getInnerHTMLValue(tmp: string) {
    return this.translate.bypassSecurityTrustHtml(tmp);
  }
  transform(value: string): SafeHtml {
    return this.translate.bypassSecurityTrustHtml(value);
  }

  onFeedClick() {
    this.location.back();
  }

  onCommentClick() {
    const dialogRef = this.dialog.open(DialogDataDialog, {
      minWidth: '500px',
      data: {
        a_id: this.a_id,
        commentSize: this.commentSize,
        comments: this.comments,
      },

    });
  }
  onFavoriteClick() {
    //
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    this.http.post<any>('/api/like_article',
      { u_id: this.uid, a_id: this.a_id },
      { headers }
    ).subscribe(
      (response) => {
        this.like = 1;
      },
      (error) => {
        console.log(error);
      }
    );
  }
}

@Component({
  selector: 'dialog-data-dialog',
  templateUrl: './dialog-data.html',
  styleUrls: ['./detail.component.css'],
  standalone: true,
  imports: [MatDialogModule, NgIf, MatIconModule, MatButtonModule, MatInputModule, FormsModule, MatListModule, MatBadgeModule, NgForOf, DatePipe],
})
export class DialogDataDialog {
  uid: string;
  like: 0;
  commentTxt: string;
  constructor(@Inject(MAT_DIALOG_DATA) public data: DialogData,
    public dialogRef: MatDialogRef<DialogDataDialog>,
    private snackBar: MatSnackBar,
    private http: HttpClient) {
    this.uid = sessionStorage.getItem('user-id');
    this.like = 0;
  }
  onCommentClick() {
  }

  ngOnInit() {
  }


  onCommentSend(txt: string) {
    //Clear test

    if (!txt || txt.length < 5) {
      this.snackBar.open('Content is required, and should be more than 5 chars', 'OK', { duration: 2000 });
      return;
    }
    let icon = sessionStorage.getItem("user-icon")
    let icon_index;
    let nickname = sessionStorage.getItem("user")
    if (icon) {
      icon_index = parseInt(icon)
    }
    let item = {
      nickname: nickname,
      iconIndex: icon,
      content: txt,
      ts: new Date()
    }
    //
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    this.http.post<any>('/api/comment_article',
      {
        fk_a_id: this.data.a_id,
        fk_u_id: this.uid,
        content: txt,
      },
      { headers }
    ).subscribe(
      (response) => {
        this.data.comments.push(item);
      },
      (error) => {
        console.log(error);
      }
    );

  }


  onSendCommClick(commentTxt: string) {

  }

  onCancelClick() {
    this.dialogRef.close();
  }
}
