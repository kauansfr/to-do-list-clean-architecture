IF OBJECT_ID('dbo.Tasks', 'U') IS NULL
    CREATE TABLE dbo.Tasks (
        TaskId INT IDENTITY(1,1) NOT NULL,
        Title NVARCHAR(255) NOT NULL,
        Description NVARCHAR(MAX) NULL,
        CreatedAt DATETIME2(3) NOT NULL
        CONSTRAINT DF_Tasks_CreatedAt DEFAULT (SYSUTCDATETIME()),
        UpdatedAt DATETIME2(3) NULL,
        CompletedAt DATETIME2(3) NULL,
        Status NVARCHAR(20) NOT NULL,
        CONSTRAINT PK_Tasks PRIMARY KEY CLUSTERED (TaskId ASC),
        CONSTRAINT CK_Tasks_Status CHECK (Status IN (N'Não iniciado', N'Em progresso', N'Concluído'))
);