import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BackofficeToolbarComponent } from './backoffice-toolbar.component';

describe('BackofficeToolbarComponent', () => {
  let component: BackofficeToolbarComponent;
  let fixture: ComponentFixture<BackofficeToolbarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BackofficeToolbarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BackofficeToolbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
