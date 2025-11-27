from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, declarative_base



SQLALCHEMY_DATABASE_URL = "mysql+pymysql://user:password@topcit-db.cjg2aaai646f.ap-northeast-2.rds.amazonaws.com:3306/topcit"

engine = create_engine(
    SQLALCHEMY_DATABASE_URL, connect_args={"check_same_thread": False}
)

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()

# DB 세션 dependency
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
