import { Component, OnDestroy, OnInit } from '@angular/core';
import { SubjectsService } from "../../services/subjects.service";
import { CommonModule } from "@angular/common";
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { SubjectCardComponent } from "../subject-card/subject-card.component";
import { BehaviorSubject, Subject, takeUntil, catchError, of } from "rxjs";
import { SubjectWithStatus } from "../../interfaces/subjectWithStatus.interface";

@Component({
  selector: 'app-subjects-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    SubjectCardComponent
  ],
  templateUrl: './subjects-list.component.html',
  styleUrls: ['./subjects-list.component.scss']
})
export class SubjectsListComponent implements OnInit, OnDestroy {
  private subjectsSubject = new BehaviorSubject<SubjectWithStatus[]>([]);
  allSubjects$ = this.subjectsSubject.asObservable();

  private destroy$ = new Subject<void>();

  constructor(
    private readonly subjectsService: SubjectsService,
  ) { }

  ngOnInit(): void {
    this.subjectsService.getAllSubjectsWithStatus()
      .pipe(
        catchError(() => of([])),
        takeUntil(this.destroy$)
      )
      .subscribe(subjects => this.subjectsSubject.next(subjects));
  }

  subscribe(subjectId: number): void {
    this.subjectsService.subscribe(subjectId)
      .pipe(
        catchError(() => of(null)),
        takeUntil(this.destroy$)
      )
      .subscribe((updatedSubjects: SubjectWithStatus[] | null) => {
        if (updatedSubjects) {
          this.subjectsSubject.next(updatedSubjects);
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.subjectsSubject.complete();
  }
}
