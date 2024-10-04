import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModificaListaPopupComponent } from './modifica-lista-popup.component';

describe('ModificaListaPopupComponent', () => {
  let component: ModificaListaPopupComponent;
  let fixture: ComponentFixture<ModificaListaPopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModificaListaPopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModificaListaPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
