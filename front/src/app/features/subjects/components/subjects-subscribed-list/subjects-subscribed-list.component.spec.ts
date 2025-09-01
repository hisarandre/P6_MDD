import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubjectsSubscribedListComponent } from './subjects-subscribed-list.component';

describe('SubjectsSubscribedListComponent', () => {
  let component: SubjectsSubscribedListComponent;
  let fixture: ComponentFixture<SubjectsSubscribedListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubjectsSubscribedListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubjectsSubscribedListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
