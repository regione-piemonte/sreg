import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModificaProfiloPopupComponent } from './modifica-profilo-popup.component';

describe('ModificaProfiloPopupComponent', () => {
  let component: ModificaProfiloPopupComponent;
  let fixture: ComponentFixture<ModificaProfiloPopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModificaProfiloPopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModificaProfiloPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
