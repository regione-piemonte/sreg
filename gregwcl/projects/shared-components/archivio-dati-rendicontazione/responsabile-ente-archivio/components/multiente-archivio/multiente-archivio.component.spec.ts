import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MultienteArchivioComponent } from './multiente-archivio.component';

describe('MultienteArchivioComponent', () => {
  let component: MultienteArchivioComponent;
  let fixture: ComponentFixture<MultienteArchivioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MultienteArchivioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MultienteArchivioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
