import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CronologiaModelliArchivioComponent } from './cronologia-modelli-archivio.component';

describe('CronologiaModelliArchivioComponent', () => {
  let component: CronologiaModelliArchivioComponent;
  let fixture: ComponentFixture<CronologiaModelliArchivioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CronologiaModelliArchivioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CronologiaModelliArchivioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
