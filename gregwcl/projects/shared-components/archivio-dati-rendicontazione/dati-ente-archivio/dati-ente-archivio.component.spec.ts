import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatiEnteArchivioComponent } from './dati-ente-archivio.component';

describe('DatiEnteArchivioComponent', () => {
  let component: DatiEnteArchivioComponent;
  let fixture: ComponentFixture<DatiEnteArchivioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatiEnteArchivioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatiEnteArchivioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
