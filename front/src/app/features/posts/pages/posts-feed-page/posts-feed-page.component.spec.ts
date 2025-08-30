import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostsFeedPageComponent } from './posts-feed-page.component';

describe('PostsFeedPageComponent', () => {
  let component: PostsFeedPageComponent;
  let fixture: ComponentFixture<PostsFeedPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PostsFeedPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PostsFeedPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
