import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MessaggioPopupComponent } from './messaggio-popup.component';

describe('MessaggioPopupComponent', () => {
  let component: MessaggioPopupComponent;
  let fixture: ComponentFixture<MessaggioPopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MessaggioPopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MessaggioPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
