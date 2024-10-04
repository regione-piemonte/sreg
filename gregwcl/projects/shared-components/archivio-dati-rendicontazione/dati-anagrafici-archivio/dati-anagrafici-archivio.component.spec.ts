import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { DatiAnagraficiArchivioComponent } from './dati-anagrafici-archivio.component';


describe('DatiAnagraficiComponent', () => {
  let component: DatiAnagraficiArchivioComponent;
  let fixture: ComponentFixture<DatiAnagraficiArchivioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatiAnagraficiArchivioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatiAnagraficiArchivioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
