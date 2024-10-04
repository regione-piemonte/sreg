import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreaProfiloPopupComponent } from './crea-profilo-popup.component';

describe('CreaProfiloPopupComponent', () => {
  let component: CreaProfiloPopupComponent;
  let fixture: ComponentFixture<CreaProfiloPopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreaProfiloPopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreaProfiloPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
