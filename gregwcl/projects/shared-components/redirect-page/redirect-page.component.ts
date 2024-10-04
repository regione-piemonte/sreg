import { Component, OnInit } from '@angular/core';
import { NavigationExtras, Router } from '@angular/router';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { UserInfoGreg } from '@greg-app/app/dto/UserInfoGreg';
import { environment } from '@greg-app/environments/environment';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'redirect-page',
  templateUrl: './redirect-page.component.html',
  styleUrls: ['./redirect-page.component.css']
})
export class RedirectPageComponent implements OnInit {
  userLogged: UserInfoGreg;

  constructor(private router: Router, public client: GregBOClient, private cookies :CookieService) {
    }

  ngOnInit() {
  }

removesessionparamter() {
		window.sessionStorage.removeItem("XSRF_SESSION_TOKEN");
		window.sessionStorage.removeItem("X-XSRF-TOKEN");
		window.sessionStorage.removeItem("XSRF-TOKEN");
		window.sessionStorage.removeItem("iride2_id");
		window.sessionStorage.removeItem("Shib-Iride-IdentitaDigitale");
		window.sessionStorage.removeItem("appDatacurrentUser");
		this.cookies.deleteAll();
		window.sessionStorage.clear();
		window.localStorage.clear();
}

  refreshPage(){
	this.removesessionparamter();
    //window.location.reload();
//	 this.client.logout().subscribe(
 //     (response: UserInfoGreg ) => {
	  window.open(environment.logoutscaduta, "_self");
//	 },
//      err => {}
 //)
  }

}
