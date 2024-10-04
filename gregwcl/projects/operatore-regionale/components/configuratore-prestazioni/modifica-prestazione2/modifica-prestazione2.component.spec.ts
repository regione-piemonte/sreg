import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModificaPrestazione2Component } from './modifica-prestazione2.component';

describe('ModificaPrestazione2Component', () => {
  let component: ModificaPrestazione2Component;
  let fixture: ComponentFixture<ModificaPrestazione2Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModificaPrestazione2Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModificaPrestazione2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
