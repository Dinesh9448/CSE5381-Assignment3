import {HttpClient} from '@angular/common/http';
import {Component, ViewChild, AfterViewInit} from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import { Page } from 'ngx-pagination/dist/pagination-controls.directive';
import {merge, Observable, of as observableOf} from 'rxjs';
import { NgbPanelChangeEvent, NgbAccordion } from '@ng-bootstrap/ng-bootstrap';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';
import { saveAs } from 'file-saver';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent  {
  title = 'cryptoui';
  @ViewChild('myaccordion', {static : true}) accordion: NgbAccordion;
  repository: Repository | null;
  users: string[] = [];
  userInfo: UserInfo | null;
  filterId: number = 0;

  myForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(3)]),
    file: new FormControl('', [Validators.required]),
    fileSource: new FormControl('', [Validators.required])
  });

  constructor(private _httpClient: HttpClient){
    this.repository = new Repository(_httpClient);
    this.repository.getUsers().subscribe(data => {
      this.users = data;
    });
  }

  beforeChange($event: NgbPanelChangeEvent) {
    console.log($event.panelId);
    this.getUserInfo($event.panelId);
  }

  getUserInfo(user : string){
    this.repository.getUserInfo(user).subscribe(data => {
      this.userInfo = data;
    });
  }

  onSelected(value: number){
    console.log(value);
    this.filterId = value;
  }

  get f(){
    return this.myForm.controls;
  }

  onFileChange(event) {

    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.myForm.patchValue({
        fileSource: file
      });
    }
  }

  addUser(value: string){
    this.repository.getUserInfo(value).subscribe(data =>{
      this.users.push(data.username);
    });
  }

  submit(){
    console.log('**********************');
    const formData = new FormData();
    formData.append('file', this.myForm.get('fileSource').value);
    let pathParam: string;
    let type: string;
    let encryptDecrypt: string;

    if(this.filterId == 1){
      type = 'AES';
      encryptDecrypt = 'encrypt';
    } else if(this.filterId == 2){
      type = 'AES';
      encryptDecrypt = 'decrypt';
    } else if(this.filterId == 3){
      type = 'RSA';
      encryptDecrypt = 'encrypt';
    } else if(this.filterId == 4){
      type = 'RSA';
      encryptDecrypt = 'decrypt';
    } else if(this.filterId == 5){
      type = 'HASH';
      encryptDecrypt = 'MD5';
    } else if(this.filterId == 6){
      type = 'HASH';
      encryptDecrypt = 'SHA-256';
    }

    pathParam = `/${type}/${encryptDecrypt}?user=${this.userInfo.username}`
    this.repository.downloadFile(pathParam, formData)
      .subscribe(res => {
        console.log(this.myForm.get('fileSource').value.name);
        const fileName = this.myForm.get('fileSource').value.name;
        saveAs(new Blob([res], {type: 'application/text'}), fileName.substring(0, fileName.indexOf('.txt')) + `_${type}_${encryptDecrypt}.txt`);
      });
  }
}

export interface UserInfo{
  username: string;
  password: string;
  aesKey: string;
  rsaPublicKey: string;

  module: string;
  publicExpo: string;
}

export class Repository{
  constructor(private _httpClient: HttpClient){}
  //href = 'https://cse6331-assignment7.azurewebsites.net/game';
  href = '/crypto';

  getUsers(): Observable<string[]>{
    return this._httpClient.get<string[]>(this.href + '/users');
  }

  getUserInfo(user: string): Observable<UserInfo>{
    return this._httpClient.get<UserInfo>(`${this.href}/userinfo?user=${user}`);
  }

  downloadFile(pathParam: string, formData: FormData) {
    return this._httpClient.post(`${this.href}/encrypt-file${pathParam}`, formData, {
      responseType: 'arraybuffer'
    });
  }

}
