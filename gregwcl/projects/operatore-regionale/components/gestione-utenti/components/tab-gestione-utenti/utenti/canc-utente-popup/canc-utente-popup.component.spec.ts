import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { CancUtentePopupComponent } from './canc-utente-popup.component';


describe('CancUtentePopupComponent', () => {
  let component: CancUtentePopupComponent;
  let fixture: ComponentFixture<CancUtentePopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CancUtentePopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CancUtentePopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
