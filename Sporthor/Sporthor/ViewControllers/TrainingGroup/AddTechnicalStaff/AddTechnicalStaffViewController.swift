//
//  AddTechnicalStaffViewController.swift
//  Sporthor
//
//  Created by derTurke on 29.10.2025.
//
//

import UIKit
import ComponentKit
import PanModal

final class AddTechnicalStaffViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: AddTechnicalStaffPresenterProtocol {
        get { return self.basePresenter as! AddTechnicalStaffPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var scrollLineView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.backgroundSub300.color
        view.setCornerRadius(2)
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(text: "Antrenör Rolünü Belirle",
                            textColor: .black,
                            textAlignment: .center,
                            font: .heading06)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var tableView: UITableView = {
        let tableView = UITableView()
        tableView.delegate = self
        tableView.dataSource = self
        tableView.backgroundColor = .clear
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.contentInset = .init(top: 16, left: 0, bottom: 16, right: 0)
        tableView.separatorStyle = .none
        return tableView
    }()
    
    private lazy var submitButton: CKButton = {
        let button = CKButton(
            delegate: self,
            title: "Devam Et",
            titleColor: DesignKitColorName.contentStrong900.color,
            buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
            cornerRadius: 23,
            font: .bold03Compact,
            tag: 1)
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
}

// MARK: - AddTechnicalStaffPresenterDelegate
extension AddTechnicalStaffViewController: AddTechnicalStaffPresenterDelegate {
    func prepareUI() {
        view.addSubview(scrollLineView)
        view.addSubview(titleLabel)
        view.addSubview(tableView)
        view.addSubview(submitButton)
        
        NSLayoutConstraint.activate([
            scrollLineView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            scrollLineView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            scrollLineView.widthAnchor.constraint(equalToConstant: 40),
            scrollLineView.heightAnchor.constraint(equalToConstant: 4),
            
            titleLabel.topAnchor.constraint(equalTo: scrollLineView.bottomAnchor, constant: 16),
            titleLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            titleLabel.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            
            tableView.topAnchor.constraint(equalTo: titleLabel.bottomAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            
            submitButton.topAnchor.constraint(equalTo: tableView.bottomAnchor),
            submitButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            submitButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            submitButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -32)
        ])
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
        }
    }
}

// MARK: - UITableViewDataSource
extension AddTechnicalStaffViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 3
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section {
        case 0:
            let cell = ProfileImageEditTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(image: presenter.image,
                      name: presenter.name,
                      buttonTitle: "",
                      isHiddenBorderView: true)
            return cell
        case 1:
            let cell = TextFieldTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(delegate: self,
                      textFieldText: presenter.role,
                      textFieldPlaceholder: "Rolü yazınız",
                      textFieldPlaceholderColor: DesignKitColorName.contentSoft600.color,
                      textFieldBackgroundColor: .clear,
                      textFieldSelectedBorderColor: .clear,
                      textFieldFont: .heading06,
                      textFieldMaxLength: 100,
                      textFieldTag: 1)
            return cell
        case 2:
            let cell = EventTypeTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.configure(delegate: self,
                           model: presenter.eventTypes, type: .role, isHiddenAddButtonType: true)
            return cell
        default:
            return UITableViewCell()
        }
    }
}

// MARK: - UITableViewDelegate
extension AddTechnicalStaffViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        guard section == 1 else { return UIView() }

        let footerView = UIView()
        footerView.backgroundColor = .clear
        
        let innerView = UIView()
        innerView.backgroundColor = DesignKitColorName.borderSoft200.color
        innerView.translatesAutoresizingMaskIntoConstraints = false
        footerView.addSubview(innerView)

        NSLayoutConstraint.activate([
            innerView.leadingAnchor.constraint(equalTo: footerView.leadingAnchor, constant: 16),
            innerView.trailingAnchor.constraint(equalTo: footerView.trailingAnchor, constant: -16),
            innerView.topAnchor.constraint(equalTo: footerView.topAnchor),
            innerView.bottomAnchor.constraint(equalTo: footerView.bottomAnchor)
        ])

        return footerView
    }
    
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        guard section == 1 else { return 0 }
        return 1
    }
}

// MARK: - CKButton
extension AddTechnicalStaffViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedCKButton(tag)
    }
}

extension AddTechnicalStaffViewController: PanModalPresentable {
    var panScrollable: UIScrollView? {
        return nil
    }
    
    var allowsDragToDismiss: Bool {
        return true
    }

    var allowsTapToDismiss: Bool {
        return true
    }
    
    var cornerRadius: CGFloat {
        return 16
    }
    
    var panModalBackgroundColor: UIColor {
        return .black.withAlphaComponent(0.4)
    }
    
    var showDragIndicator: Bool {
        return false
    }
    
    var longFormHeight: PanModalHeight {
        return .maxHeight
    }
}

// MARK: - TextFieldTableViewCellDelegate
extension AddTechnicalStaffViewController: TextFieldTableViewCellDelegate {
    func textFieldDidEndEditing(text: String, tag: Int, indexPath: IndexPath?) {
        presenter.textFieldDidEndEditing(text: text, tag: tag)
    }
}

extension AddTechnicalStaffViewController: EventTypeTableViewCellDelegate {
    func didSelectEventType(with model: EventTypeModel) {
        presenter.didSelectEventType(with: model)
    }
}
