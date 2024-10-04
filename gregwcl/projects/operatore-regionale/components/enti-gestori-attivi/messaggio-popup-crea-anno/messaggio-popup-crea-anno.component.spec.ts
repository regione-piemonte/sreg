import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MessaggioPopupCreaAnnoComponent } from './messaggio-popup-crea-anno.component';

describe('MessaggioPopupCreaAnnoComponent', () => {
  let component: MessaggioPopupCreaAnnoComponent;
  let fixture: ComponentFixture<MessaggioPopupCreaAnnoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MessaggioPopupCreaAnnoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MessaggioPopupCreaAnnoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
