import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectToolbarComponent } from './project-toolbar.component';

describe('ProjectToolbarComponent', () => {
  let component: ProjectToolbarComponent;
  let fixture: ComponentFixture<ProjectToolbarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectToolbarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectToolbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
