from pydantic import BaseModel

class NoteCreate(BaseModel):
    username: str
    content: str
    important: bool = False

class NoteOut(BaseModel):
    id: int
    username: str
    content: str
    important: bool

    class Config:
        from_attributes = True  # Pydantic v2