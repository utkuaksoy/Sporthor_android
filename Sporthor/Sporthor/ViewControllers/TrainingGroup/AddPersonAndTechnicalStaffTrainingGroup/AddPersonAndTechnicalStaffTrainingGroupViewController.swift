//
//  AddPersonAndTechnicalStaffTrainingGroupViewController.swift
//  Sporthor
//
//  Created by derTurke on 27.10.2025.
//
//

import UIKit
import ComponentKit

final class AddPersonAndTechnicalStaffTrainingGroupViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: AddPersonAndTechnicalStaffTrainingGroupPresenterProtocol {
        get { return self.basePresenter as! AddPersonAndTechnicalStaffTrainingGroupPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var clubInfoView: CKClubInfoView = {
        let view = CKClubInfoView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var tableView: UITableView = {
        let tableView = UITableView()
        tableView.dataSource = self
        tableView.delegate = self
        tableView.backgroundColor = .clear
        tableView.separatorStyle = .none
        tableView.contentInset = .init(top: 16, left: 0, bottom: 16, right: 0)
        tableView.translatesAutoresizingMaskIntoConstraints = false
        return tableView
    }()
    
    private lazy var continueButton: CKButton = {
        let ckButton = CKButton(delegate: self,
                                title: "Anasayfa",
                                titleColor: DesignKitColorName.contentStrong900.color,
                                buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
                                cornerRadius: 23,
                                font: .bold03Compact,
                                tag: 1)
        ckButton.heightAnchor.constraint(equalToConstant: 46).isActive = true
        ckButton.translatesAutoresizingMaskIntoConstraints = false
        return ckButton
    }()
    
    private lazy var bottomStackView: CKStackView = {
        let stackView = CKStackView(spacing: 12)
        stackView.isLayoutMarginsRelativeArrangement = true
        stackView.layoutMargins = .init(top: 0, left: 16, bottom: 32, right: 16)
        stackView.addArrangedSubviews([continueButton])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
}

// MARK: - AddPersonAndTechnicalStaffTrainingGroupPresenterDelegate
extension AddPersonAndTechnicalStaffTrainingGroupViewController: AddPersonAndTechnicalStaffTrainingGroupPresenterDelegate {
    func setupView() {
        view.addSubview(clubInfoView)
        view.addSubview(tableView)
        view.addSubview(bottomStackView)
        
        NSLayoutConstraint.activate([
            clubInfoView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 12),
            clubInfoView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            clubInfoView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            
            tableView.topAnchor.constraint(equalTo: clubInfoView.bottomAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            
            bottomStackView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor),
            bottomStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            bottomStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            bottomStackView.topAnchor.constraint(equalTo: tableView.bottomAnchor)
        ])
    }
    
    func prepareClubInfo(_ trainingGroup: TrainingGroupResponse) {
        clubInfoView.bind(image: trainingGroup.logo,
                          name: trainingGroup.teamName,
                          info: trainingGroup.groupName)
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
        }
    }
}

// MARK: - UITableViewDataSource
extension AddPersonAndTechnicalStaffTrainingGroupViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        presenter.cellModels.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let model = presenter.cellModels[indexPath.section]
        let cell = AddPersonInfoAndButtonTableViewCell.dequeue(from: tableView, at: indexPath)
        cell.bind(delegate: self,
                  title: model.name,
                  info: model.value,
                  buttonTitle: model.detail,
                  tag: indexPath.section)
        return cell
    }
}

// MARK: - UITableViewDelegate
extension AddPersonAndTechnicalStaffTrainingGroupViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        let view = UIView()
        view.backgroundColor = .clear
        return view
    }
    
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        return 16
    }
}

extension AddPersonAndTechnicalStaffTrainingGroupViewController: AddPersonInfoAndButtonTableViewCellDelegate {
    func didTappedAddPersonInfoAndButtonTableViewCell(_ tag: Int) {
        presenter.didTappedCellButton(tag)
    }
}

extension AddPersonAndTechnicalStaffTrainingGroupViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedCKButton(tag)
    }
}
