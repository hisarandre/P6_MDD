import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateMeFormComponent } from './update-me-form.component';

describe('UpdateMeFormComponent', () => {
  let component: UpdateMeFormComponent;
  let fixture: ComponentFixture<UpdateMeFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateMeFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateMeFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
