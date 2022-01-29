IF NOT EXISTS (SELECT name FROM master.sys.databases WHERE name = N'GadgetDb')
	CREATE DATABASE [GadgetDb];
GO

USE [GadgetDb]



IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'App')
BEGIN
	EXEC('Create Schema App;')
END
GO


CREATE LOGIN gadgetdbuser WITH PASSWORD = 'GadgetTestPwd1234';
CREATE USER gadgetdbuser FOR LOGIN gadgetdbuser WITH DEFAULT_SCHEMA= App
GO


GRANT SELECT,UPDATE,INSERT,EXEC ON SCHEMA :: App  
    TO gadgetdbuser
GO


IF NOT EXISTS (SELECT * 
                 FROM INFORMATION_SCHEMA.TABLES 
                 WHERE TABLE_SCHEMA = 'App' 
                 AND  TABLE_NAME = 'GadgetImportResult')
BEGIN
	CREATE TABLE [App].[GadgetImportResult] (
		gadgetImportJobId  INT IDENTITY(1,1) PRIMARY KEY,
		operationStartDateTime DateTime2,
		operationEndDateTime DateTime2,
		lastModifiedStartDate DateTime2,
		lastModifiedEndDate DateTime2,
		totalRecords INT,
	)
END
GO

IF NOT EXISTS (SELECT * 
                 FROM INFORMATION_SCHEMA.TABLES 
                 WHERE TABLE_SCHEMA = 'App' 
                 AND  TABLE_NAME = 'Gadgets')
BEGIN
	CREATE TABLE [App].[Gadgets] (
		gadget_id varchar(32) NOT NULL PRIMARY KEY,
		creationDate DateTime,
		lastModifiedDate DateTime,
		)
END
GO

IF NOT EXISTS (SELECT * 
                 FROM INFORMATION_SCHEMA.TABLES 
                 WHERE TABLE_SCHEMA = 'App' 
                 AND  TABLE_NAME = 'Gadgets_Staging')
BEGIN
	CREATE TABLE [App].[Gadgets_Staging] (
		gadget_id varchar(32) NOT NULL PRIMARY KEY,
		creationDate DateTime,
		lastModifiedDate DateTime,
		)
END
GO



CREATE OR ALTER PROCEDURE App.GadgetMergeUpsert
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

	MERGE Gadgets as TARGET
	USING Gadgets_Staging as SOURCE
	ON
		(SOURCE.gadget_id = TARGET.gadget_id)
	WHEN MATCHED 
		THEN 
		Update SET TARGET.gadget_id = SOURCE.gadget_id
	WHEN NOT MATCHED 
		THEN INSERT 
			   ([gadget_id]
			   ,[creationDate]
			   ,[lastModifiedDate])
		 VALUES
			   (gadget_id
			   ,creationDate
			   ,lastModifiedDate);

		Delete from Gadgets_Staging;
END
GO