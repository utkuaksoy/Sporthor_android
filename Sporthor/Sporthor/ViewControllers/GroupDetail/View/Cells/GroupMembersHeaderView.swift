import UIKit
import ComponentBaseKit
import ComponentKit
import DesignKit

final class GroupMembersHeaderView: UICollectionReusableView, ReusableView {
    // MARK: - Private UI Elements
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.text = "Grup Üyeleri"
        label.font = .bold04Compact
        label.textColor = ColorName.contentStrong900.color
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var addButton: UIButton = {
        let button = UIButton(type: .custom)
        button.setTitle("Kişi Ekle", for: .normal)
        button.titleLabel?.font = .interTight400
        button.setTitleColor(ColorName.contentStrong900.color, for: .normal)
        button.setImage(.userPlus, for: .normal)
        button.tintColor = ColorName.contentStrong900.color
        button.addTarget(self, action: #selector(addButtonTapped(_:)), for: .touchUpInside)
        button.translatesAutoresizingMaskIntoConstraints = false
        let spacing: CGFloat = 8
        button.imageEdgeInsets = UIEdgeInsets(top: 0, left: -spacing/2, bottom: 0, right: spacing/2)
        button.titleEdgeInsets = UIEdgeInsets(top: 0, left: spacing/2, bottom: 0, right: -spacing/2)
        return button
    }()
    
    // MARK: - Properties
    var addMemberTapped: (() -> Void)?
    
    // MARK: - Initialization
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Actions
    @objc private func addButtonTapped(_ sender: UIButton) {
        addMemberTapped?()
    }
}

// MARK: - Setup
private extension GroupMembersHeaderView {
    func setupViews() {
        backgroundColor = .white
        addSubview(titleLabel)
        addSubview(addButton)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            titleLabel.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 16),
            titleLabel.centerYAnchor.constraint(equalTo: centerYAnchor),
            
            addButton.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -16),
            addButton.centerYAnchor.constraint(equalTo: centerYAnchor)
        ])
    }
} 
