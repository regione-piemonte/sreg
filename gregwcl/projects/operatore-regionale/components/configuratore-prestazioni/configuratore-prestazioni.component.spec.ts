import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfiguratorePrestazioniComponent } from './configuratore-prestazioni.component';

describe('ConfiguratorePrestazioniComponent', () => {
  let component: ConfiguratorePrestazioniComponent;
  let fixture: ComponentFixture<ConfiguratorePrestazioniComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfiguratorePrestazioniComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfiguratorePrestazioniComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
