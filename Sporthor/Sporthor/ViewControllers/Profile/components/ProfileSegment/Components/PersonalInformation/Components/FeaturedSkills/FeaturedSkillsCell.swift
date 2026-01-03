//
//  FeaturedSkillsCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 20.03.2025.
//

import ComponentKit
import ComponentBaseKit
import DesignKit
import UIKit

final class FeaturedSkillsCell: UICollectionViewCell, ReusableView {
    
    // MARK: - Private UI Elements

    private lazy var containerStackView: UIStackView = {
        let stackView = UIStackView(arrangedSubviews: [titleLabel, skillsContainerView])
        stackView.axis = .vertical
        stackView.spacing = 12
        stackView.alignment = .fill
        stackView.distribution = .fill
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(
            textColor: ColorName.contentStrong900.color,
            font: .bold03Compact
        )
        label.setContentHuggingPriority(.required, for: .vertical)
        label.setContentCompressionResistancePriority(.required, for: .vertical)
        return label
    }()
    
    private lazy var skillsContainerView: UIView = {
        let view = UIView()
        view.setCornerRadius(8)
        view.backgroundColor = ColorName.backgroundWeak100.color
        view.layer.masksToBounds = true
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var skillsContainerStackView: CKStackView = {
        let stackView = CKStackView(
            axis: .vertical,
            distribution: .fill,
            alignment: .fill,
            spacing: 24
        )
        stackView.layer.masksToBounds = true
        stackView.addArrangedSubviews([skillsCollectionView, detailsGridView])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var skillsCollectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        layout.minimumInteritemSpacing = 8
        layout.minimumLineSpacing = 8
        layout.sectionInset = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
        layout.estimatedItemSize = UICollectionViewFlowLayout.automaticSize
        
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.backgroundColor = .clear
        collectionView.showsHorizontalScrollIndicator = false
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.register(SkillTabCell.self, forCellWithReuseIdentifier: SkillTabCell.reuseIdentifier)
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        collectionView.contentInset = .init(top: .zero, left: 16, bottom: .zero, right: 16)
        return collectionView
    }()
    
    private lazy var detailsGridView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var detailsStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = 16
        stackView.alignment = .fill
        stackView.distribution = .fill
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    // MARK: - Private Properties

    private var dataModel: FeaturedSkillsDataModel?
    private var selectedSkillId: String?
    
    // MARK: - Initializer

    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Configure

    func configure(with dataModel: FeaturedSkillsDataModel?) {
        self.dataModel = dataModel
        self.titleLabel.text = dataModel?.title
        
        if selectedSkillId == nil {
            selectedSkillId = dataModel?.skills?.first?.id
        }
        
        skillsCollectionView.reloadData()
        updateDetailsView()
    }
    
    // MARK: - Private Methods

    private func setupViews() {
        contentView.addSubview(containerStackView)
        skillsContainerView.addSubview(skillsContainerStackView)
        detailsGridView.addSubview(detailsStackView)
        
        NSLayoutConstraint.activate([
            containerStackView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 24),
            containerStackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            containerStackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            containerStackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            
            skillsContainerView.heightAnchor.constraint(greaterThanOrEqualToConstant: 194),
            
            skillsContainerStackView.topAnchor.constraint(equalTo: skillsContainerView.topAnchor, constant: 16),
            skillsContainerStackView.leadingAnchor.constraint(equalTo: skillsContainerView.leadingAnchor),
            skillsContainerStackView.trailingAnchor.constraint(equalTo: skillsContainerView.trailingAnchor),
            skillsContainerStackView.bottomAnchor.constraint(equalTo: skillsContainerView.bottomAnchor, constant: -16),
            
            skillsCollectionView.heightAnchor.constraint(equalToConstant: 40),
            
            detailsStackView.topAnchor.constraint(equalTo: detailsGridView.topAnchor),
            detailsStackView.leadingAnchor.constraint(equalTo: detailsGridView.leadingAnchor, constant: 16),
            detailsStackView.trailingAnchor.constraint(equalTo: detailsGridView.trailingAnchor, constant: -16),
            detailsStackView.bottomAnchor.constraint(equalTo: detailsGridView.bottomAnchor)
        ])
    }
    
    private func updateDetailsView() {
        detailsStackView.arrangedSubviews.forEach { $0.removeFromSuperview() }
        
        guard let selectedSkill = dataModel?.skills?.first(where: { $0.id == selectedSkillId }) else { return }
        
        let detailRows = createDetailRows(from: selectedSkill.details ?? [])
        detailRows.forEach { row in
            detailsStackView.addArrangedSubview(row)
        }
    }
    
    private func createDetailRows(from details: [DetailedSkillModel]) -> [UIView] {
        let validDetails = details.filter { detail in
            guard let value = detail.value else { return false }
            return !value.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
        }
        
        let limitedDetails = Array(validDetails.prefix(4))
        
        if limitedDetails.isEmpty {
            return [createEmptyStateView()]
        }
        
        var rows: [UIView] = []
        var currentRow: [DetailedSkillModel] = []
        
        for detail in limitedDetails {
            currentRow.append(detail)
            
            if currentRow.count == 2 {
                rows.append(createDetailRow(with: currentRow))
                currentRow = []
            }
        }
        
        if !currentRow.isEmpty {
            rows.append(createDetailRow(with: currentRow))
        }
        
        return rows
    }
    
    private func createDetailRow(with details: [DetailedSkillModel]) -> UIStackView {
        let rowStack = UIStackView()
        rowStack.axis = .horizontal
        rowStack.spacing = 16
        rowStack.distribution = .fillEqually
        rowStack.alignment = .top
        
        details.forEach { detail in
            let detailView = createDetailView(with: detail)
            rowStack.addArrangedSubview(detailView)
        }
        
        if details.count == 1 {
            let emptyView = UIView()
            rowStack.addArrangedSubview(emptyView)
        }
        
        return rowStack
    }
    
    private func createEmptyStateView() -> UIView {
        let containerView = UIView()
        
        let label = CKLabel(
            text: "Henüz detay girilmemiş",
            textColor: ColorName.contentSoft600.color,
            textAlignment: .center,
            font: .interTight400
        )
        
        containerView.addSubview(label)
        label.translatesAutoresizingMaskIntoConstraints = false
        
        NSLayoutConstraint.activate([
            label.topAnchor.constraint(equalTo: containerView.topAnchor, constant: 16),
            label.leadingAnchor.constraint(equalTo: containerView.leadingAnchor),
            label.trailingAnchor.constraint(equalTo: containerView.trailingAnchor),
            label.bottomAnchor.constraint(equalTo: containerView.bottomAnchor, constant: -16)
        ])
        
        return containerView
    }
    
    private func createDetailView(with detail: DetailedSkillModel) -> UIView {
        let containerView = UIView()
        containerView.backgroundColor = .clear

        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = 4
        stackView.alignment = .leading
        stackView.distribution = .fill
        stackView.translatesAutoresizingMaskIntoConstraints = false

        let titleLabel = CKLabel(
            text: detail.title ?? "",
            textColor: ColorName.contentSoft600.color,
            font: .body04Compact
        )
        titleLabel.setContentHuggingPriority(.required, for: .horizontal)

        let valueText = detail.value?.trimmingCharacters(in: .whitespacesAndNewlines) ?? ""

        let valueLabel = CKLabel(
            text: !valueText.isEmpty ? valueText : "-",
            textColor: ColorName.contentStrong900.color,
            font: .bold03Compact
        )
        valueLabel.setContentHuggingPriority(.required, for: .horizontal)

        stackView.addArrangedSubview(titleLabel)
        stackView.addArrangedSubview(valueLabel)
        containerView.addSubview(stackView)

        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: containerView.topAnchor),
            stackView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor),
            stackView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor),
            stackView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor)
        ])

        return containerView
    }
}

// MARK: - UICollectionViewDataSource

extension FeaturedSkillsCell: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return dataModel?.skills?.count ?? .zero
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(
            withReuseIdentifier: SkillTabCell.reuseIdentifier,
            for: indexPath
        ) as? SkillTabCell,
        let skill = dataModel?.skills?[indexPath.item] else {
            return UICollectionViewCell()
        }
        
        let isSelected = skill.id == selectedSkillId
        cell.configure(with: skill, isSelected: isSelected)
        return cell
    }
}

// MARK: - UICollectionViewDelegate

extension FeaturedSkillsCell: UICollectionViewDelegate {
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        guard let selectedSkill = dataModel?.skills?[indexPath.item] else { return }
        selectedSkillId = selectedSkill.id
        updateDetailsView()
        collectionView.reloadData()
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            collectionView.scrollToItem(
                at: indexPath,
                at: .centeredHorizontally,
                animated: true
            )
        }
    }
}

// MARK: - UICollectionViewDelegateFlowLayout

extension FeaturedSkillsCell: UICollectionViewDelegateFlowLayout {
    func collectionView(
        _ collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        sizeForItemAt indexPath: IndexPath
    ) -> CGSize {
        guard let skill = dataModel?.skills?[indexPath.item] else {
            return CGSize(width: 90, height: 40)
        }
        
        let text = skill.name ?? ""
        let labelWidth = text.width(withConstrainedHeight: 40, font: .bold04Compact)
        
        let calculatedWidth = 16 + 8 + labelWidth + 32
        
        return CGSize(width: max(calculatedWidth, 90), height: 40)
    }
}
