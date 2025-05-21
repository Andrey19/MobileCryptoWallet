package com.ar11.mobilecryptowallet.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ar11.mobilecryptowallet.dto.Project

@Entity
data class ProjectEntity (
    @PrimaryKey
    val projectName: String,
    val projectDescription: String,
    val image: String,
    val imageUrl: String,
    val projectCost: Double,
) {
    fun toDto() = Project(projectName,projectDescription,image,imageUrl,projectCost)

    companion object {
        fun fromDto(dto: Project) =
            ProjectEntity(dto.projectName, dto.projectDescription, dto.image,
                dto.imageUrl, dto.projectCost)
    }
}

fun List<ProjectEntity>.toDto(): List<Project> = map(ProjectEntity::toDto)
fun List<Project>.toEntity(): List<ProjectEntity> = map { ProjectEntity.fromDto(it) }
