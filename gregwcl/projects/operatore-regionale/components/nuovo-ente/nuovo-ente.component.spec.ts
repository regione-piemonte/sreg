import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NuovoEnteComponent } from './nuovo-ente.component';


describe('NuovoEnteComponent', () => {
  let component: NuovoEnteComponent;
  let fixture: ComponentFixture<NuovoEnteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NuovoEnteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NuovoEnteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
