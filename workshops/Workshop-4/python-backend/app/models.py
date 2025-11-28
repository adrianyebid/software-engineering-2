from sqlalchemy import Column, Integer, String, Boolean
from .database import Base

class Note(Base):
    __tablename__ = "notes"

    id = Column(Integer, primary_key=True, index=True)
    username = Column(String, index=True, nullable=False)
    content = Column(String, nullable=False)
    important = Column(Boolean, default=False)