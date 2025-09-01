import {Component, OnInit, HostListener, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { Observable, filter } from 'rxjs';
import { SessionService } from 'src/app/core/services/session.service';
import { CommonModule, AsyncPipe } from '@angular/common';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import {HttpClientModule} from "@angular/common/http";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  standalone: true,
  imports: [
    CommonModule,
    MatSidenavModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    AsyncPipe,
    RouterLink,
    RouterLinkActive,
    RouterOutlet
  ]
})
export class HeaderComponent implements OnInit {
  isMobile: boolean = false;
  isSidenavOpen: boolean = false;
  showNavbar: boolean = true;
  isLoggedIn$: Observable<boolean>;

  constructor(
    public router: Router,
    private readonly sessionService: SessionService
  ) {
    this.isLoggedIn$ = this.sessionService.$isLogged();
  }

  ngOnInit(): void {
    this.checkScreenSize();

    this.router.events
      .pipe(filter((event): event is NavigationEnd => event instanceof NavigationEnd))
      .subscribe(event => {
        this.showNavbar = event.urlAfterRedirects !== '/home';
      });
  }

  @HostListener('window:resize')
  onResize() {
    this.checkScreenSize();
  }

  checkScreenSize(): void {
    this.isMobile = window.innerWidth < 768;
    if (!this.isMobile) {
      this.isSidenavOpen = false;
    }
  }

  logout(): void {
    this.sessionService.logOut();
    this.router.navigate(['/home']);
    this.closeSidenav();
  }

  toggleSidenav(): void {
    this.isSidenavOpen = !this.isSidenavOpen;
    document.body.style.overflow = this.isSidenavOpen ? 'hidden' : '';
  }

  closeSidenav(): void {
    this.isSidenavOpen = false;
    document.body.style.overflow = '';
  }

  navigateAndClose(path: string) {
    this.router.navigate([path]);
    this.closeSidenav();
  }
}
