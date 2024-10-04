import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfiguratoreFnpsComponent } from './configuratore-fnps.component';

describe('EntiGestoriAttiviComponent', () => {
  let component: ConfiguratoreFnpsComponent;
  let fixture: ComponentFixture<ConfiguratoreFnpsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfiguratoreFnpsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfiguratoreFnpsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
