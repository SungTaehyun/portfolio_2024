package com.lhs.dto;

public class AttFileDto {
	private int fileIdx;
	private int typeSeq;
	private int boardSeq;
	private int fileSize;
	private String fileName;
	private String fakeFileName;
	private String fileType;
	private String saveLoc;
	private String createDtm;
	public int getFileIdx() {
		return fileIdx;
	}
	public void setFileIdx(int fileIdx) {
		this.fileIdx = fileIdx;
	}
	public int getTypeSeq() {
		return typeSeq;
	}
	public void setTypeSeq(int typeSeq) {
		this.typeSeq = typeSeq;
	}
	public int getBoardSeq() {
		return boardSeq;
	}
	public void setBoardSeq(int boardSeq) {
		this.boardSeq = boardSeq;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFakeFileName() {
		return fakeFileName;
	}
	public void setFakeFileName(String fakeFileName) {
		this.fakeFileName = fakeFileName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getSaveLoc() {
		return saveLoc;
	}
	public void setSaveLoc(String saveLoc) {
		this.saveLoc = saveLoc;
	}
	public String getCreateDtm() {
		return createDtm;
	}
	public void setCreateDtm(String createDtm) {
		this.createDtm = createDtm;
	}
	@Override
	public String toString() {
		return "FileDto [fileIdx=" + fileIdx + ", typeSeq=" + typeSeq + ", boardSeq=" + boardSeq + ", fileSize="
				+ fileSize + ", fileName=" + fileName + ", fakeFileName=" + fakeFileName + ", fileType=" + fileType
				+ ", saveLoc=" + saveLoc + ", createDtm=" + createDtm + "]";
	}

}
