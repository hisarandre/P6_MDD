import { Component, OnInit, OnDestroy, HostListener, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { Observable, Subscription, filter } from 'rxjs';
import { SessionService } from 'src/app/core/services/session.service';
import { CommonModule, AsyncPipe } from '@angular/common';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

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
export class HeaderComponent implements OnInit, OnDestroy {
  isMobile = false;
  isSidenavOpen = false;
  showNavbar = true;
  isLoggedIn$: Observable<boolean>;

  private readonly mobileBreakpoint = 768;
  private subscription = new Subscription();

  constructor(
    public router: Router,
    private readonly sessionService: SessionService
  ) {
    this.isLoggedIn$ = this.sessionService.$isLogged();
  }

  ngOnInit(): void {
    this.checkScreenSize();

    this.subscription.add(
      this.router.events
        .pipe(filter((event): event is NavigationEnd => event instanceof NavigationEnd))
        .subscribe(event => {
          this.showNavbar = event.urlAfterRedirects !== '/home';
        })
    );
  }

  @HostListener('window:resize')
  onResize(): void {
    this.checkScreenSize();
  }

  toggleSidenav(): void {
    this.isSidenavOpen = !this.isSidenavOpen;
    document.body.style.overflow = this.isSidenavOpen ? 'hidden' : '';
  }

  closeSidenav(): void {
    this.isSidenavOpen = false;
    document.body.style.overflow = this.isSidenavOpen ? 'hidden' : '';
  }

  logout(): void {
    this.sessionService.logOut();
    this.router.navigate(['/home']);
  }

  handleLogoutAndClose(): void {
    this.logout();
    this.closeSidenav();
  }

  private checkScreenSize(): void {
    const wasMobile = this.isMobile;
    this.isMobile = window.innerWidth < this.mobileBreakpoint;

    // Close sidenav when switch from mobile to desktop
    if (wasMobile && !this.isMobile && this.isSidenavOpen) {
      this.closeSidenav();
    }
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
