import { Component, OnDestroy, OnInit } from '@angular/core';
import { SubjectsService } from "../../services/subjects.service";
import { CommonModule } from "@angular/common";
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { SubjectCardComponent } from "../subject-card/subject-card.component";
import { Observable, of, switchMap, Subject, takeUntil } from "rxjs";
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
  allSubjects$: Observable<SubjectWithStatus[]> = of();
  private destroy$ = new Subject<void>();

  constructor(
    private readonly subjectsService: SubjectsService,
  ) { }

  ngOnInit(): void {
    this.allSubjects$ = this.subjectsService.getAllSubjects();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  subscribe(subjectId: number): void {
    this.subjectsService.subscribe(subjectId).pipe(
      switchMap(() => this.subjectsService.getAllSubjects()),
      takeUntil(this.destroy$)
    ).subscribe({
      next: (subjects) => {
        this.allSubjects$ = of(subjects);
      },
      error: () => {}
    });
  }
}
